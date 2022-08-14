package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.config.VnPayConfig;
import com.example.springboot_cy_marketplace.dto.*;
import com.example.springboot_cy_marketplace.entity.*;
import com.example.springboot_cy_marketplace.model.Constant;
import com.example.springboot_cy_marketplace.repository.*;
import com.example.springboot_cy_marketplace.services.IOrderService;
import com.example.springboot_cy_marketplace.services.PayPalServices;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.posadskiy.currencyconverter.CurrencyConverter;
import com.posadskiy.currencyconverter.config.ConfigBuilder;
import com.posadskiy.currencyconverter.enums.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements IOrderService {
    @Value("${server.host.fe.user}")
    public String homePage;
    @Autowired
    PayPalServices payPalServices;
    @Autowired
    MailService mailService;
    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private CartServiceImpl cartService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IProductClassifiedRepository productClassifiedRepository;
    @Autowired
    private INoticesLocalRepository noticesLocalRepository;
    @Autowired
    private INotifyCategoryRepository notifyCategoryRepository;
    @Value("${payment.success.paypal.url}")
    private String paypalSuccessUrl;
    @Value("${payment.success.vnpay.url}")
    private String vnpaySuccessUrl;
    @Value("${payment.failed.url}")
    private String paymentFailedUrl;
    @Value("${currency_converter_api_key}")
    private String currencyConverterApiKey;

    @Value("${open_exchange_rates_api_key}")
    private String openExchangeRatesApiKey;

    @Value("${currency_layer_api_key}")
    private String currencyLayerApiKey;

    @Autowired
    private ProductStatisticalService productStatisticalService;

    /*
     * @author: Manh Tran
     * @since: 23/06/2022 2:27 CH
     * @description-VN: Đặt đơn hàng mới.
     * @description-EN: Place new order.
     * @param: userId - Mã người dùng đặt hàng.
     * @param: paymentMethod - Phương thức thanh toán.
     * @param: addressId - Mã địa chỉ giao hàng.
     * @return:
     *
     * */
    public String placedOrder(PlaceOrderDTO placeOrder) {
        CartDTO listProductToCheckout = cartService.getListProductToCheckout(placeOrder.getUserId());
        if (listProductToCheckout == null) {
            return "Can not find list product to checkout!";
        }

        UserEntity userEntity = userRepository.findById(placeOrder.getUserId()).orElse(null);
        if (userEntity == null) {
            return null;
        }

        // Số tiền thanh toán phải lớn hơn số tiền giảm giá.
        if(placeOrder.getTotalDiscount() > listProductToCheckout.getTotalPrice()) {
            return "Total discount must be less than total price!";
        }

        // Giá vốn của đơn hàng
        double costPrice = 0;
        // Lợi nhuận của đơn hàng
        double profit = 0;
        List<OrderItemEntity> listOrderItemEntity = new ArrayList<>();
        // Kiểm tra số lượng đặt mua không được lớn hơn tồn kho.
        // Check quantity of order is not greater than stock.
        for (CartProductDTO item : listProductToCheckout.getProductList()) {
            ProductClassifiedEntity productClassified = productClassifiedRepository.findById(item.getProductClassifiedId())
                    .orElse(null);
            if (productClassified == null) {
                continue;
            }
            if (productClassified.getAmount() < item.getQuantity()) {
                return "Product " + productClassified.getProductEntity().getName() + " is out of stock!";
            }

            // Cập nhật số lượng sản phẩm bán ra
            productStatisticalService.updateBuy(productClassified.getProductEntity().getId());

            String classified1 = productClassified.getClassifyName1();
            String classified2 = productClassified.getClassifyName2();
            OrderItemEntity orderItemEntity = new OrderItemEntity(productClassified, item.getQuantity(), item.getNewPrice(),
                     classified1 == null ? "" : classified1, classified2 == null ? "" : classified2);
            listOrderItemEntity.add(orderItemEntity);

            // Tính lợi nhuận
            profit += (Double.parseDouble(productClassified.getNewPrice()) * item.getQuantity()) -
                    (productClassified.getProductEntity().getCostPrice() * item.getQuantity());

            // Tính giá vốn
            costPrice += productClassified.getProductEntity().getCostPrice() * item.getQuantity();
        }

        boolean isPayByPayPal = false;
        boolean isPayByVnPay = false;
        OrderEntity orderEntity = new OrderEntity(userEntity, listOrderItemEntity);
        // Lưu thông tin địa chỉ nhận hàng
        // Save information of address to receive goods.
        orderEntity.setProvinceName(placeOrder.getProvinceName());
        orderEntity.setCityName(placeOrder.getCityName());
        orderEntity.setDistrictName(placeOrder.getDistrictName());

        // Lưu lại số tiền giảm giá (miễn phí vận chuyển, mã giảm giá,...)
        Double totalDiscount = placeOrder.getTotalDiscount() == null ? 0 : placeOrder.getTotalDiscount();
        orderEntity.setTotalDiscount(totalDiscount);
        orderEntity.setDiscountProduct(placeOrder.getDiscountProduct());
        orderEntity.setDiscountFreeShip(placeOrder.getDiscountFreeShip());

        // Lưu lại lợi nhuận
        orderEntity.setProfit(profit - totalDiscount);

        // Lưu lại giá vốn
        orderEntity.setCostPrice(costPrice);

        String homeAddress = placeOrder.getHomeAddress().substring(0, placeOrder.getHomeAddress().indexOf(","));
        orderEntity.setHomeAddress(homeAddress);

        // Lưu lại phương thức thanh toán của đơn hàng
        if (placeOrder.getPaymentMethod().equalsIgnoreCase(Constant.ORDER_PAY_PAYPAL)) {
            isPayByPayPal = true;
            orderEntity.setStatus(Constant.ORDER_WAITING_PAYMENT);
            orderEntity.setPaymentMethod(Constant.ORDER_PAY_PAYPAL);
        } else if (placeOrder.getPaymentMethod().equalsIgnoreCase(Constant.ORDER_PAY_VNPAY)) {
            isPayByVnPay = true;
            orderEntity.setStatus(Constant.ORDER_WAITING_PAYMENT);
            orderEntity.setPaymentMethod(Constant.ORDER_PAY_VNPAY);
        } else {
            orderEntity.setStatus(Constant.ORDER_PICKING);
            orderEntity.setPaymentMethod(Constant.ORDER_PAY_COD);
        }
        orderEntity.setTotalQuantity(listProductToCheckout.getTotalQuantity());
        orderEntity.setShippingFee(placeOrder.getShippingFee());
        orderEntity.setTotalPrice(listProductToCheckout.getTotalPrice());

        // Nếu khách hàng thanh toán trực tuyến
        // If customer pay online
        if (isPayByPayPal) {
            try {
                String paymentUrl = this.payByPayPal(listProductToCheckout.getTotalPrice() + placeOrder.getShippingFee() - totalDiscount);
                this.setTokenForOrder(orderEntity, paymentUrl);
                orderRepository.save(orderEntity);

                // Dọn sạch giỏ hàng
                this.clearCartAfterOrder(placeOrder.getUserId(), listProductToCheckout);

                // Trừ tồn kho
                this.minusProductStock(listProductToCheckout);
                return paymentUrl;
            } catch (PayPalRESTException e) {
                e.printStackTrace();
            }
        } else if (isPayByVnPay) {
            String vnPayUrl = this.payByVNPay(listProductToCheckout.getTotalPrice().longValue() +
                    placeOrder.getShippingFee().longValue() - totalDiscount.longValue());
            this.setTokenForOrder(orderEntity, vnPayUrl);
            orderRepository.save(orderEntity);

            // Dọn sạch giỏ hàng
            this.clearCartAfterOrder(placeOrder.getUserId(), listProductToCheckout);

            // Trừ tồn kho
            this.minusProductStock(listProductToCheckout);
            return vnPayUrl;
        }
        OrderEntity result = orderRepository.save(orderEntity);
        // Nếu khách hàng thanh toán tiền mặt - If customer pay cash.
        // Dọn sạch giỏ hàng - Clear cart after order.
        this.clearCartAfterOrder(placeOrder.getUserId(), listProductToCheckout);

        // Trừ tồn kho - Subtract stock.
        this.minusProductStock(listProductToCheckout);

        // Gửi mail xác nhận - Send mail confirm.
        this.sendMailToUser(orderEntity, Constant.ORDER_PAY_COD, null, null);

        // Tạo thông báo mới cho người dùng
        NoticesLocalEntity notice = new NoticesLocalEntity();
        notice.setTitle("Thông báo từ YD-Market");
        notice.setContent("YD-Market đã nhận đơn hàng #" + orderEntity.getId());
        notice.setUserEntity(orderEntity.getUserEntity());
        notice.setNotifyCategoryEntity(this.notifyCategoryRepository.findById(3L).orElse(null));
        noticesLocalRepository.save(notice);

        // Trả về đường dẫn chi tiết đơn hàng - Return url detail order.
        return homePage + "/order/" + result.getId();
    }

    /*
     * @author: Manh Tran
     * @since: 23/06/2022 2:25 CH
     * @description-VN: Kích hoạt đơn hàng thanh toán trực tuyến.
     * @description-EN: Activate order payment online.
     * @param: paymentId - Mã giao dịch thanh toán bên PayPal.
     * @param: token - Token thanh toán do PayPal/VnPay tạo.
     * @param: payerId - Mã tài khoản thanh toán bên PayPal.
     * @param: vnp_ResponseCode - Mã trạng thái giao dịch VnPay gửi về (00 là thành công).
     * @return:
     *
     * */
    public OrderDetailDTO onlinePaymentResult(PayPalResultDTO payPalResult, VnPayResultDTO vnPayResult) {
        Optional<OrderEntity> checkOrder = orderRepository.findByPaymentToken(payPalResult != null ? payPalResult.getToken() :
                vnPayResult.getVnp_TxnRef() + vnPayResult.getVnp_Amount());
        if (checkOrder.isEmpty()) {
            return null;
        }
        OrderEntity orderEntity = checkOrder.get();
        if (vnPayResult != null) {
            if (vnPayResult.getVnp_ResponseCode().equals("00")) {
                orderEntity.setStatus(Constant.ORDER_PICKING);
                orderEntity.setPaymentToken(null);

                // Gửi mail xác nhận - Send mail confirm.
                this.sendMailToUser(orderEntity, Constant.ORDER_PAY_VNPAY, vnPayResult, null);
                orderRepository.save(orderEntity);
                return entityToDTO(orderEntity);
            }
        }
        try {
            assert payPalResult != null;
            Payment payment = payPalServices.executePayment(payPalResult.getPaymentId(), payPalResult.getPayerId());
            if (payment.getState().equals("approved")) {
                orderEntity.setStatus(Constant.ORDER_PICKING);
                orderEntity.setPaymentToken(null);
                orderRepository.save(orderEntity);
                payPalResult.setPaymentId(payment.getId());
                Double totalAmount = orderEntity.getTotalPrice() + orderEntity.getShippingFee();
                CurrencyConverter converter = new CurrencyConverter(
                        new ConfigBuilder()
                                .currencyConverterApiApiKey(currencyConverterApiKey)
                                .currencyLayerApiKey(currencyLayerApiKey)
                                .openExchangeRatesApiKey(openExchangeRatesApiKey)
                                .build()
                );
                payPalResult.setTotalAmount(totalAmount * converter.rate(Currency.VND, Currency.USD));
                // Gửi mail xác nhận - Send mail confirm.
                this.sendMailToUser(orderEntity, Constant.ORDER_PAY_PAYPAL, null, payPalResult);
                return entityToDTO(orderEntity);
            } else {
                return null;
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * @author: Manh Tran
     * @since: 23/06/2022 2:24 CH
     * @description-VN: Lấy danh sách đơn hàng của người dùng (có phân trang).
     * @description-EN: Get list order of user.
     * @param: userId - Mã người dùng muốn lấy danh sách đơn hàng.
     * @param: pageable - Thông tin phân trang.
     * @return:
     *
     * */
    public Page<OrderDetailDTO> getOrderByUserId(Long userId, Pageable pageable) {
        Pageable pageable2 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createDate"));
        Page<OrderEntity> orderEntities = orderRepository.findByUserEntity_Id(userId, pageable2);
        return orderEntities.map(this::entityToDTO);
    }

    /*
     * @author: Manh Tran
     * @since: 23/06/2022 2:23 CH
     * @description-VN: Lấy thông tin chi tiết đơn hàng.
     * @description-EN: Get order detail.
     * @param: orderId - Mã đơn hàng cần lấy thông tin.
     * @return:
     *
     * */
    public OrderDetailDTO getOrderByOrderId(Long orderId) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
        if (orderEntity.isEmpty()) {
            return null;
        }
        return entityToDTO(orderEntity.get());
    }

    /*
     * @author: Manh Tran
     * @since: 23/06/2022 2:23 CH
     * @description-VN: Huỷ đơn hàng.
     * @description-EN: Cancel order.
     * @param: orderId - Mã đơn hàng muốn huỷ.
     * @return:
     *
     * */
    public boolean cancelOrder(Long orderId) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
        if (orderEntity.isEmpty()) {
            return false;
        }
        if (orderEntity.get().getStatus().equals(Constant.ORDER_SHIPPING)) {
            return false;
        }
        orderEntity.get().setStatus(Constant.ORDER_CANCEL);
        this.addProductStock(orderEntity.get());
        orderRepository.save(orderEntity.get());
        OrderEntity orderGet = orderEntity.get();
        // Tạo thông báo mới cho người dùng
        NoticesLocalEntity notice = new NoticesLocalEntity();
        notice.setTitle("Thông báo từ YD-Market");
        notice.setContent("Đơn hàng #" + orderGet.getId() + " đã bị huỷ!");
        notice.setUserEntity(orderGet.getUserEntity());
        notice.setNotifyCategoryEntity(this.notifyCategoryRepository.findById(3L).orElse(null));
        noticesLocalRepository.save(notice);
        return true;
    }

    /*
     * @author: Manh Tran
     * @since: 23/06/2022 2:22 CH
     * @description-VN: Lọc danh sách đơn hàng theo trạng thái và mã người dùng (có phân trang).
     * @description-EN: Filter list order by status and user id (with pagination).
     * @param: userId - Mã người dùng.
     * @param: status - Trạng thái đơn hàng.
     * @param: pageable - Phân trang.
     * @return:
     *
     * */
    public Page<OrderDetailDTO> filterOrderByStatus(Long userId, String status, Pageable pageable) {
        Pageable pageable2 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createDate"));
        if (status == null || status.equalsIgnoreCase("ALL")) {
            return orderRepository.findByUserEntity_Id(userId, pageable2).map(this::entityToDTO);
        }
        return orderRepository.findByStatusAndUserEntity_Id(status, userId, pageable2).map(this::entityToDTO);
    }

    /*
     * @author: Manh Tran
     * @since: 23/06/2022 2:21 CH
     * @description-VN: Tạo liên kết thanh toán VNPay.
     * @description-EN: Create link payment VNPay.
     * @param: totalPrice - Tổng tiền cần thanh toán (Việt Nam đồng).
     * @return:
     *
     * */
    public String payByVNPay(Long totalPrice) {
        String vnp_OrderInfo = "Thanh toan don hang tai YD-Market";
        String vnp_TxnRef = VnPayConfig.getRandomNumber(8);
        String bank_code = "NCB";

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "ATM";
        String vnp_IpAddr = "0:0:0:0:0:0:0:1";
        String vnp_TmnCode = VnPayConfig.vnp_TmnCode;
        Long amount = totalPrice * 100;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bank_code != null && !bank_code.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bank_code);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "vn";
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Date dt = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(dt);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        Calendar cldvnp_ExpireDate = Calendar.getInstance();
        cldvnp_ExpireDate.add(Calendar.SECOND, 30);
        Date vnp_ExpireDateD = cldvnp_ExpireDate.getTime();
        String vnp_ExpireDate = formatter.format(vnp_ExpireDateD);

        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                try {
                    //hashData.append(fieldValue); //sử dụng và 2.0.0 và 2.0.1 checksum sha256
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString())); //sử dụng v2.1.0  check sum sha512
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return VnPayConfig.vnp_PayUrl + "?" + queryUrl;
    }

    /*
     * @author: Manh Tran
     * @since: 23/06/2022 2:20 CH
     * @description-VN: Tạo liên kết thanh toán PayPal.
     * @description-EN: Create link payment PayPal.
     * @param: totalPrice - Tổng tiền cần thanh toán (Việt Nam đồng).
     * @return:
     *
     * */
    public String payByPayPal(Double totalPrice) throws PayPalRESTException {
        CurrencyConverter converter = new CurrencyConverter(
                new ConfigBuilder()
                        .currencyConverterApiApiKey(currencyConverterApiKey)
                        .currencyLayerApiKey(currencyLayerApiKey)
                        .openExchangeRatesApiKey(openExchangeRatesApiKey)
                        .build()
        );
        double vndToUSD = totalPrice * converter.rate(Currency.VND, Currency.USD);
        Payment payment = payPalServices.createPayment(vndToUSD, "USD", Constant.ORDER_PAY_PAYPAL,
                "Sale", "Payment for order at YD-Market", homePage + paymentFailedUrl,
                homePage + paypalSuccessUrl);
        for (Links link : payment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                return link.getHref();
            }
        }
        return null;
    }

    /*
     * @author: Manh Tran
     * @since: 23/06/2022 2:19 CH
     * @description-VN: Xoá hết sản phẩm trong giỏ hàng sau khi người dùng đặt hàng.
     * @description-EN: Delete all product in cart after user order.
     * @param: userId - Mã người dùng đặt hàng.
     * @param: items - Danh sách sản phẩm đặt hàng.
     * @return:
     *
     * */
    public void clearCartAfterOrder(Long userId, CartDTO items) {
        for (CartProductDTO product : items.getProductList()) {
            AddToCartDTO addToCartDTO = new AddToCartDTO();
            addToCartDTO.setUserId(userId);
            addToCartDTO.setProductClassifiedId(product.getProductClassifiedId());
            addToCartDTO.setQuantity(0);
            cartService.updateCart(addToCartDTO);
        }
    }

    /*
     * @author: Manh Tran
     * @since: 23/06/2022 2:18 CH
     * @description-VN: Trừ tồn kho sản phẩm sau khi người dùng đặt hàng.
     * @description-EN: Decrease product stock after user order.
     * @param: listProduct - Chứa danh sách các sản phẩm được đặt.
     * @return:
     *
     * */
    public void minusProductStock(CartDTO listProduct) {
        for (CartProductDTO item : listProduct.getProductList()) {
            Optional<ProductClassifiedEntity> productClassifiedEntity = productClassifiedRepository.findById(item.getProductClassifiedId());
            if (productClassifiedEntity.isPresent()) {
                productClassifiedEntity.get().setAmount(productClassifiedEntity.get().getAmount() - item.getQuantity());
                productClassifiedRepository.save(productClassifiedEntity.get());
            }
        }
    }

    /*
     * @author: Manh Tran
     * @since: 23/06/2022 2:17 CH
     * @description-VN: Tăng tồn kho sản phẩm sau khi người dùng huỷ đơn hàng.
     * @description-EN: Increase product stock after user cancel order.
     * @param: orderEntity - Đơn hàng bị huỷ.
     * @return:
     *
     * */
    public void addProductStock(OrderEntity orderEntity) {
        List<OrderItemEntity> items = orderEntity.getItems();
        for (OrderItemEntity item : items) {
            Optional<ProductClassifiedEntity> productClassifiedEntity = productClassifiedRepository.findById(item.getItem().getId());
            if (productClassifiedEntity.isPresent()) {
                productClassifiedEntity.get().setAmount(productClassifiedEntity.get().getAmount() + item.getQuantity());
                productClassifiedRepository.save(productClassifiedEntity.get());
            }
        }
    }

    /*
     * @author: Manh Tran
     * @since: 23/06/2022 2:15 CH
     * @description-VN: Tạo liên kết cho người dùng thanh toán lại đơn hàng.
     * @description-EN: Create link for user to pay again order.
     * @param: orderId - Mã đơn hàng muốn tạo lại liên kết thanh toán.
     * @return:
     *
     * */
    public String repayOrder(Long orderId) {
        String paymentLink = "";
        if (this.orderRepository.findById(orderId).isPresent()) {
            OrderEntity orderEntity = this.orderRepository.findById(orderId).get();
            if (orderEntity.getStatus().equals(Constant.ORDER_WAITING_PAYMENT)) {
                String paymentMethod = orderEntity.getPaymentMethod();
                if (paymentMethod.equals(Constant.ORDER_PAY_VNPAY)) {
                    paymentLink = this.payByVNPay(orderEntity.getTotalPrice().longValue());
                    this.setTokenForOrder(orderEntity, paymentLink);
                    this.orderRepository.save(orderEntity);
                } else if (paymentMethod.equals(Constant.ORDER_PAY_PAYPAL)) {
                    try {
                        paymentLink = this.payByPayPal(orderEntity.getTotalPrice());
                    } catch (PayPalRESTException exception) {
                        exception.printStackTrace();
                    }
                    this.setTokenForOrder(orderEntity, paymentLink);
                    this.orderRepository.save(orderEntity);
                }
            } else {
                return "This order has been paid!";
            }
        } else {
            return "Can not find order!";
        }
        return paymentLink;
    }

    /*
     * @author: Manh Tran
     * @since: 23/06/2022 2:15 CH
     * @description-VN: Cài token thanh toán VnPay hoặc PayPal cho đơn hàng.
     * @description-EN: Set token for VnPay or PayPal for order.
     * @param: orderEntity - Đơn hàng muốn cài token.
     * @param: paymentLink - Liên kết thanh toán.
     * @return:
     *
     * */
    public void setTokenForOrder(OrderEntity orderEntity, String paymentUrl) {
        String token = "";
        if (orderEntity.getPaymentMethod().equals(Constant.ORDER_PAY_PAYPAL)) {
            int startIndex = paymentUrl.lastIndexOf('=');
            token = paymentUrl.substring(startIndex + 1);
        } else if (orderEntity.getPaymentMethod().equals(Constant.ORDER_PAY_VNPAY)) {
            int refStart = paymentUrl.indexOf("vnp_TxnRef");
            int refEnd = paymentUrl.indexOf("&", refStart);
            String ref = paymentUrl.substring(refStart + 11, refEnd);

            int amountStart = paymentUrl.indexOf("vnp_Amount");
            int amountEnd = paymentUrl.indexOf("&", amountStart);
            String amount = paymentUrl.substring(amountStart + 11, amountEnd);
            token = ref + amount;
        }
        orderEntity.setPaymentToken(token);
    }

    /*
     * @author: Manh Tran
     * @since: 23/06/2022 4:18 CH
     * @description-VN: Gửi email xác nhận đơn hàng về người dùng.
     * @description-EN: Send email confirm order to user.
     * @param:
     * @return:
     *
     * */
    public void sendMailToUser(OrderEntity orderEntity, String paymentMethod, VnPayResultDTO vnPayResult,
                               PayPalResultDTO payPalResult) {
        MailDTO mailDTO = new MailDTO();
        mailDTO.setMailTo(orderEntity.getUserEntity().getEmail());
        mailDTO.setSubject("Thanh toán đơn hàng #" + orderEntity.getId() + " tại YD-Market thành công!");
        Map<String, Object> model = new HashMap<String, Object>();
        mailDTO.setProps(model);
        switch (paymentMethod) {
            case Constant.ORDER_PAY_VNPAY:
                model.put("transactionResult", vnPayResult.getVnp_ResponseCode().equals("00") ? "Thành công" : "Thất bại");
                model.put("order", orderEntity);
                model.put("vnp_Amount", orderEntity.getTotalPrice() + orderEntity.getShippingFee());
                try {
                    mailService.customTemplateEmail(mailDTO, Constant.ORDER_PAY_VNPAY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case Constant.ORDER_PAY_PAYPAL:
                model.put("order", orderEntity);
                model.put("payPalResult", payPalResult);
                try {
                    mailService.customTemplateEmail(mailDTO, Constant.ORDER_PAY_PAYPAL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case Constant.ORDER_PAY_COD:
                mailDTO.setSubject("YD-Market đã nhận đơn hàng #" + orderEntity.getId() + " của bạn!");
                model.put("order", orderEntity);
                try {
                    mailService.customTemplateEmail(mailDTO, Constant.ORDER_PAY_COD);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case Constant.ORDER_PICKING:
                mailDTO.setSubject("Đơn hàng #" + orderEntity.getId() + " đang được xử lý!");
                model.put("order", orderEntity);
                model.put("message", "Đơn hàng của bạn đã được tiếp nhận và đang được xử lý!");
                try {
                    mailService.customTemplateEmail(mailDTO, Constant.ORDER_PAY_COD);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case Constant.ORDER_CANCEL:
                mailDTO.setSubject("Đơn hàng #" + orderEntity.getId() + " đã bị huỷ!");
                model.put("order", orderEntity);
                model.put("message", "YD-Market rất tiếc khi phải thông báo rằng \"Đơn hàng của bạn đã bị huỷ!\"");
                try {
                    mailService.customTemplateEmail(mailDTO, Constant.ORDER_PAY_COD);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case Constant.ORDER_SHIPPING:
                mailDTO.setSubject("Đơn hàng #" + orderEntity.getId() + " đang giao tới bạn!");
                model.put("order", orderEntity);
                model.put("message", "Đơn hàng của bạn đang được vận chuyển. Thời gian giao hàng dự kiến: " +
                        LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")).plusDays(3).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                try {
                    mailService.customTemplateEmail(mailDTO, Constant.ORDER_PAY_COD);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case Constant.ORDER_DONE:
                mailDTO.setSubject("Đơn hàng #" + orderEntity.getId() + " đã giao thành công!");
                model.put("order", orderEntity);
                model.put("message", "Hi vọng sẽ được phục vụ bạn trong những đơn hàng tiếp theo!");
                try {
                    mailService.customTemplateEmail(mailDTO, Constant.ORDER_PAY_COD);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        System.out.println("Send mail to user successfully!");
    }

    /*
     * @author: Manh Tran
     * @since: 24/06/2022 8:24 SA
     * @description-VN: Lấy danh sách tất cả đơn hàng.
     * @description-EN: Get all order.
     * @param:
     * @return:
     *
     * */

    @Override
    public Page<OrderDetailDTO> findAll(Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createDate");
        Pageable pageableSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return this.orderRepository.findAll(pageableSort).map(this::entityToDTO);
    }

    /*
     * @author: Manh Tran
     * @since: 24/06/2022 8:34 SA
     * @description-VN: Đổi trạng thái đơn hàng.
     * @description-EN: Change order status.
     * @param:
     * @return:
     *
     * */
    public String changeOrderStatus(Long orderId, int statusCode) {
        Optional<OrderEntity> optionalOrderEntity = this.orderRepository.findById(orderId);
        if (optionalOrderEntity.isPresent()) {
            if (optionalOrderEntity.get().getStatus().equals(Constant.ORDER_DONE)) {
                return "Đơn hàng đã giao thành công, không thể thay đổi trạng thái!";
            }
            OrderEntity orderEntity = optionalOrderEntity.get();
            switch (statusCode) {
                case 1:
                    orderEntity.setStatus(Constant.ORDER_PICKING);
                    break;
                case 2:
                    orderEntity.setStatus(Constant.ORDER_CANCEL);
                    break;
                case 3:
                    orderEntity.setStatus(Constant.ORDER_SHIPPING);
                    break;
                case 4:
                    orderEntity.setStatus(Constant.ORDER_DONE);
                    this.updateTotalSold(orderEntity);
                    break;
                default:
                    return "Order status code not found!";
            }
            // Gửi email thông báo về người mua
            this.sendMailToUser(orderEntity, orderEntity.getStatus(), null, null);

            // Lưu lại thông tin đơn hàng
            this.orderRepository.save(orderEntity);

            // Tạo thông báo mới cho người dùng
            NoticesLocalEntity notice = new NoticesLocalEntity();
            notice.setTitle("Thông báo từ YD-Market");
            notice.setContent("Đơn hàng #" + orderEntity.getId() + " " + orderEntity.getStatus().toLowerCase());
            notice.setUserEntity(orderEntity.getUserEntity());
            notice.setNotifyCategoryEntity(this.notifyCategoryRepository.findById(3L).orElse(null));
            noticesLocalRepository.save(notice);
        } else {
            return "Can not find order!";
        }
        return "Change order status successfully!";
    }

    /*
     * @author: Manh Tran
     * @since: 24/06/2022 4:50 CH
     * @description-VN: Cập nhật số lượng sản phẩm đã bán.
     * @description-EN:
     * @param:
     * @return:
     *
     * */
    public void updateTotalSold(OrderEntity orderEntity) {
        for (OrderItemEntity item : orderEntity.getItems()) {
            ProductClassifiedEntity productClassified = item.getItem();
            productClassified.setTotalSold(productClassified.getTotalSold() + item.getQuantity());
            this.productClassifiedRepository.save(productClassified);
        }
    }

    /*
     * @author: Manh Tran
     * @since: 28/06/2022 2:10 CH
     * @description-VN: Tìm đơn hàng theo từ khoá (nhiều tiêu chí).
     * @description-EN: Find order by keyword (many criteria).
     * @param: keyword - từ khoá tìm kiếm.
     * @param: pageable - tham số phân trang.
     * @return:
     *
     * */
    public Page<OrderDetailDTO> searchOrder(String keyword, Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createDate");
        Pageable pageableSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return this.orderRepository.findByKeyword(keyword, pageableSort).map(this::entityToDTO);
    }

    /*
     * @author: Manh Tran
     * @since: 28/06/2022 3:21 CH
     * @description-VN: Lọc đơn hàng theo trạng thái (gửi mã số, không gửi chuỗi).
     * @description-EN: Filter order by status (send code, not send string).
     * @param:
     * @return:
     *
     * */
    public Page<OrderDetailDTO> filterOrderByStatusAdmin(int statusCode, Pageable pageable) {
        String status = "";
        if (statusCode == 1) {
            status = Constant.ORDER_PICKING;
        } else if (statusCode == 2) {
            status = Constant.ORDER_CANCEL;
        } else if (statusCode == 3) {
            status = Constant.ORDER_SHIPPING;
        } else if (statusCode == 4) {
            status = Constant.ORDER_DONE;
        } else if (statusCode == 5) {
            status = Constant.ORDER_WAITING_PAYMENT;
        }
        return this.orderRepository.findByStatus(status, pageable).map(this::entityToDTO);
    }

    /*
     * @author: Manh Tran
     * @since: 28/06/2022 3:26 CH
     * @description-VN: Lọc đơn hàng theo khoảng thời gian.
     * @description-EN: Filter order by time range.
     * @param: startDate - thời gian bắt đầu.
     * @param: endDate - thời gian kết thúc.
     * @return:
     *
     * */
    public Page<OrderDetailDTO> filterOrderByTime(String startDate, String endDate, Pageable pageable) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startFormat = sdf.parse(startDate);
            Date endFormat = sdf.parse(endDate);
            if(startFormat.equals(endFormat)){
                endFormat.setHours(23);
                endFormat.setMinutes(59);
                endFormat.setSeconds(59);
            }
            return this.orderRepository.findByCreateDateBetween(startFormat, endFormat, pageable).map(this::entityToDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * @author: Manh Tran
     * @since: 28/06/2022 3:28 CH
     * @description-VN: Lọc đơn hàng theo phương thức thanh toán.
     * @description-EN: Filter order by payment method.
     * @param:
     * @return:
     *
     * */
    public Page<OrderDetailDTO> filterOrderByPaymentMethod(int paymentCode, Pageable pageable) {
        String paymentMethod = "";
        if (paymentCode == 1) {
            paymentMethod = Constant.ORDER_PAY_COD;
        } else if (paymentCode == 2) {
            paymentMethod = Constant.ORDER_PAY_PAYPAL;
        } else if (paymentCode == 3) {
            paymentMethod = Constant.ORDER_PAY_VNPAY;
        }
        return this.orderRepository.findByPaymentMethod(paymentMethod, pageable).map(this::entityToDTO);
    }

    /*
     * @author: Manh Tran
     * @since: 30/06/2022 8:13 SA
     * @description-VN: Thống kê chung về đơn hàng.
     * @description-EN: Statistic about order.
     * @param: startDate - thời gian bắt đầu.
     * @param: endDate - thời gian kết thúc.
     * @return:
     *
     * */
    public OrderStatisticDTO statisticOrder(String startDate, String endDate) {
        Date nowDate = new Date();
        String fromDateStr = "";
        String toDateStr = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (startDate != null) {
            try {
                fromDateStr = sdf.format(sdf.parse(startDate));
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        if (endDate != null) {
            try {
                toDateStr = sdf.format(sdf.parse(endDate));
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } else {
            toDateStr = sdf.format(nowDate);
        }

        OrderStatisticDTO orderStatisticDTO = new OrderStatisticDTO();
        orderStatisticDTO.setTotalOrder(this.orderRepository.count());
        orderStatisticDTO.setTotalOrderProcessing(this.orderRepository.countByStatusAndTime(fromDateStr, toDateStr, Constant.ORDER_PICKING));
        orderStatisticDTO.setTotalOrderCancel(this.orderRepository.countByStatusAndTime(fromDateStr, toDateStr, Constant.ORDER_CANCEL));
        orderStatisticDTO.setTotalOrderShipping(this.orderRepository.countByStatusAndTime(fromDateStr, toDateStr, Constant.ORDER_SHIPPING));
        orderStatisticDTO.setTotalOrderDone(this.orderRepository.countByStatusAndTime(fromDateStr, toDateStr, Constant.ORDER_DONE));
        orderStatisticDTO.setTotalWaitingForPayment(this.orderRepository.countByStatusAndTime(fromDateStr, toDateStr, Constant.ORDER_WAITING_PAYMENT));
        orderStatisticDTO.setTotalPayPalPayment(this.orderRepository.countByPaymentMethodAndTime(fromDateStr, toDateStr, Constant.ORDER_PAY_PAYPAL));
        orderStatisticDTO.setTotalVnPayPayment(this.orderRepository.countByPaymentMethodAndTime(fromDateStr, toDateStr, Constant.ORDER_PAY_VNPAY));
        orderStatisticDTO.setTotalCodPayment(this.orderRepository.countByPaymentMethodAndTime(fromDateStr, toDateStr, Constant.ORDER_PAY_COD));
        return orderStatisticDTO;
    }

    /*
     * @author: Manh Tran
     * @since: 30/06/2022 8:01 SA
     * @description-VN: Thống kê tổng doanh thu đơn hàng theo từng khung giờ.
     * @description-EN: Statistical total revenue by hour.
     * @param:
     * @return:
     *
     * */
    public List<Double> statisticalByHour(String fromDayTime, String toDayTime, int type) {
        Date nowDate = new Date();
        String fromDateStr = "";
        String toDateStr = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (fromDayTime != null) {
            try {
                fromDateStr = sdf.format(sdf.parse(fromDayTime));
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        if (toDayTime != null) {
            try {
                toDateStr = sdf.format(sdf.parse(toDayTime));
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } else {
            toDateStr = sdf.format(nowDate);
        }

        int nowHour = Calendar.getInstance(TimeZone.getTimeZone("Asia/Saigon")).get(Calendar.HOUR_OF_DAY);
        List<Double> result = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String yesterdayFromStr = "";
        String yesterdayToStr = "";
        String todayFromStr = "";
        String today6AMStr = "";
        String today12AMStr = "";
        String today6PMStr = "";
        String today1159PMStr = "";
        try {
            yesterdayFromStr = dtf.format(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).minusDays(1).withHour(0).withMinute(0).withSecond(0));
            yesterdayToStr = dtf.format(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).minusDays(1).withHour(23).withMinute(59).withSecond(59));
            todayFromStr = dtf.format(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).withHour(0).withMinute(0).withSecond(0));
            today6AMStr = dtf.format(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).withHour(6).withMinute(0).withSecond(0));
            today12AMStr = dtf.format(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).withHour(12).withMinute(0).withSecond(0));
            today6PMStr = dtf.format(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).withHour(18).withMinute(0).withSecond(0));
            today1159PMStr = dtf.format(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).withHour(23).withMinute(59).withSecond(59));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        switch (type) {
            case 1:
                this.statisticalRevenue(result, nowHour, yesterdayFromStr, yesterdayToStr, todayFromStr, today6AMStr,
                        today12AMStr, today6PMStr, today1159PMStr);
                break;
            case 2:
                this.statisticalProfit(result, nowHour, yesterdayFromStr, yesterdayToStr, todayFromStr, today6AMStr,
                        today12AMStr, today6PMStr, today1159PMStr);
                break;
        }
        return result;
    }

    public void statisticalRevenue(List<Double> result, int nowHour, String yesterdayFromStr, String yesterdayToStr,
                                   String todayFromStr, String today6AMStr, String today12AMStr, String today6PMStr,
                                   String today1159PMStr) {
        Double revenue0 = this.orderRepository.sumRevenueByTime(yesterdayFromStr, yesterdayToStr);
        Double revenue1 = this.orderRepository.sumRevenueByTime(todayFromStr, today6AMStr);
        Double revenue2 = this.orderRepository.sumRevenueByTime(today6AMStr, today12AMStr);
        Double revenue3 = this.orderRepository.sumRevenueByTime(today12AMStr, today6PMStr);
        Double revenue4 = this.orderRepository.sumRevenueByTime(today6PMStr, today1159PMStr);
        result.add(0, revenue0 == null ? 0 : revenue0);
        if (nowHour <= 6) {
            result.add(1, revenue1 == null ? 0 : revenue1);
        } else if (nowHour <= 12) {
            result.add(1, revenue1 == null ? 0 : revenue1);
            result.add(2, revenue2 == null ? 0 : revenue2);
        } else if (nowHour <= 18) {
            result.add(1, revenue1 == null ? 0 : revenue1);
            result.add(2, revenue2 == null ? 0 : revenue2);
            result.add(3, revenue3 == null ? 0 : revenue3);
        } else {
            result.add(1, revenue1 == null ? 0 : revenue1);
            result.add(2, revenue2 == null ? 0 : revenue2);
            result.add(3, revenue3 == null ? 0 : revenue3);
            result.add(4, revenue4 == null ? 0 : revenue4);
        }
    }

    public void statisticalProfit(List<Double> result, int nowHour, String yesterdayFromStr, String yesterdayToStr,
                                  String todayFromStr, String today6AMStr, String today12AMStr, String today6PMStr,
                                  String today1159PMStr) {
        Double profit0 = this.orderRepository.sumProfitByTime(yesterdayFromStr, yesterdayToStr);
        Double profit1 = this.orderRepository.sumProfitByTime(todayFromStr, today6AMStr);
        Double profit2 = this.orderRepository.sumProfitByTime(today6AMStr, today12AMStr);
        Double profit3 = this.orderRepository.sumProfitByTime(today12AMStr, today6PMStr);
        Double profit4 = this.orderRepository.sumProfitByTime(today6PMStr, today1159PMStr);
        result.add(0, profit0 == null ? 0 : profit0);
        if (nowHour <= 6) {
            result.add(1, profit1 == null ? 0 : profit1);
        } else if (nowHour <= 12) {
            result.add(1, profit1 == null ? 0 : profit1);
            result.add(2, profit2 == null ? 0 : profit2);
        } else if (nowHour <= 18) {
            result.add(1, profit1 == null ? 0 : profit1);
            result.add(2, profit2 == null ? 0 : profit2);
            result.add(3, profit3 == null ? 0 : profit3);
        } else {
            result.add(1, profit1 == null ? 0 : profit1);
            result.add(2, profit2 == null ? 0 : profit2);
            result.add(3, profit3 == null ? 0 : profit3);
            result.add(4, profit4 == null ? 0 : profit4);
        }
    }

    /*
     * @author: Manh Tran
     * @since: 23/06/2022 2:17 CH
     * @description-VN: Chuyển từ entity về DTO.
     * @description-EN: Convert entity to DTO.
     * @param: orderEntity - Thông tin đơn hàng.
     * @return:
     *
     * */
    public OrderDetailDTO entityToDTO(OrderEntity orderEntity) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        String address = orderEntity.getHomeAddress() + ", "
                + orderEntity.getDistrictName() + ", "
                + orderEntity.getCityName() + ", "
                + orderEntity.getProvinceName();
        return OrderDetailDTO.builder()
                .orderId(orderEntity.getId())
                .userId(orderEntity.getUserEntity().getId())
                .status(orderEntity.getStatus())
                .createdAt(sdf.format(orderEntity.getCreateDate()))
                .totalPrice(orderEntity.getTotalPrice())
                .shippingFee(orderEntity.getShippingFee())
                .totalDiscount(orderEntity.getTotalDiscount())
                .paymentMethod(orderEntity.getPaymentMethod())
                .currency(orderEntity.getPaymentMethod().equals(Constant.ORDER_PAY_PAYPAL) ? "USD" : "VND")
                .intent("Mua hàng")
                .totalPayment(orderEntity.getTotalPrice() + orderEntity.getShippingFee() - orderEntity.getTotalDiscount())
                .discountProduct(orderEntity.getDiscountProduct() == null ? 0 : orderEntity.getDiscountProduct())
                .discountFreeShip(orderEntity.getDiscountFreeShip() == null ? 0 : orderEntity.getDiscountFreeShip())
                .receiverName(orderEntity.getUserEntity().getFullName())
                .receiverEmail(orderEntity.getUserEntity().getEmail())
                .receiverPhone(orderEntity.getUserEntity().getPhoneNumber())
                .receiverAddress(address)
                .productList(orderEntity.getItems().stream().map(orderItemEntity -> {
                    ProductClassifiedEntity productClassifiedEntity = orderItemEntity.getItem();
                    return CartProductDTO.builder()
                            .productId(productClassifiedEntity.getProductEntity().getId())
                            .productName(productClassifiedEntity.getProductEntity().getName())
                            .productCoverImage(productClassifiedEntity.getProductEntity().getCoverImage())
                            .productClassifiedId(productClassifiedEntity.getId())
                            .productClassifiedBy01(productClassifiedEntity.getProductEntity().getClassifiedBy01())
                            .productClassifiedName1(orderItemEntity.getClassified1())
                            .productClassifiedBy02(productClassifiedEntity.getProductEntity().getClassifiedBy02())
                            .productClassifiedName2(orderItemEntity.getClassified2())
                            .classifiedImage(productClassifiedEntity.getImage())
                            .newPrice(orderItemEntity.getPrice())
                            .oldPrice(Double.parseDouble(productClassifiedEntity.getOldPrice()))
                            .discount(productClassifiedEntity.getDiscount())
                            .amount(productClassifiedEntity.getAmount())
                            .quantity(orderItemEntity.getQuantity())
                            .height(productClassifiedEntity.getProductEntity().getHeight())
                            .width(productClassifiedEntity.getProductEntity().getWidth())
                            .length(productClassifiedEntity.getProductEntity().getLength())
                            .weight(productClassifiedEntity.getProductEntity().getWeight())
                            .build();
                }).collect(Collectors.toList()))
                .totalQuantity(orderEntity.getTotalQuantity())
                .description("Thanh toán đơn hàng tại YD-Market!")
                .reviewStatus(orderEntity.isReviewStatus())
                .build();
    }

    @Override
    public List<OrderDetailDTO> findAll() {
        return null;
    }

    @Override
    public OrderDetailDTO findById(Long id) {
        return null;
    }

    @Override
    public OrderDetailDTO add(OrderEntity dto) {

        return null;
    }

    @Override
    public List<OrderDetailDTO> add(List<OrderEntity> dto) {
        return null;
    }

    @Override
    public OrderDetailDTO update(OrderEntity dto) {
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public boolean deleteByIds(List<Long> id) {
        return false;
    }

}
