package com.example.springboot_cy_marketplace.web;

import com.example.springboot_cy_marketplace.config.BannerExcelExporterConfig;
import com.example.springboot_cy_marketplace.config.Position;
import com.example.springboot_cy_marketplace.dto.*;
import com.example.springboot_cy_marketplace.entity.OrderEntity;
import com.example.springboot_cy_marketplace.entity.UserEntity;
import com.example.springboot_cy_marketplace.repository.IOrderRepository;
import com.example.springboot_cy_marketplace.services.impl.OrderServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/order")
public class OrderResource {
    @Autowired
    OrderServiceImpl orderService;

    /*
    * @author: Manh Tran
    * @since: 23/06/2022 1:35 CH
    * @description-VN: Đặt đơn hàng cho người dùng.
    * @description-EN: Place an order for user.
    * @param: userId - Mã người dùng,
    * @param: paymentMethod - Hình thức thanh toán,
    * @param: addressId - Mã địa chỉ giao hàng.
    * @return:
    *
    * */
    @RolesAllowed(Position.BUYER)
    @PostMapping(value = "/place-order")
    public Object placeOrder(@RequestBody PlaceOrderDTO placeOrderDTO) {
        String result = this.orderService.placedOrder(placeOrderDTO);
        int code = result != null ? 200 : 400;
        return ResponseDTO.show(code, "Place order", code == 200 ? result : null);
    }

    /*
    * @author: Manh Tran
    * @since: 23/06/2022 1:32 CH
    * @description-VN: Gửi lại kết quả giao dịch thanh toán trực tuyến.
    * @description-EN: Send the result of the online payment transaction.
    * @param: paymentId - Mã giao dịch thanh toán trực tuyến (PayPal),
    * @param: token - Mã token được gửi từ PayPal/VnPay lúc thanh toán,
    * @param: PayerID - Mã người thanh toán (PayPal),
    * @param: vnp_ResponseCode - Mã kết quả giao dịch trả về từ VnPay (00 là thành công).
    * @return:
    *
    * */
    @RolesAllowed(Position.BUYER)
    @PostMapping(value = "/paypal-result")
    public Object onlinePaymentResult(@RequestBody PayPalResultDTO payPalResultDTO) {
        OrderDetailDTO orderDetailDTO = this.orderService.onlinePaymentResult(payPalResultDTO, null);
        int code = orderDetailDTO != null ? 200 : 404;
        return ResponseDTO.show(code, "PayPal result", code == 200 ? orderDetailDTO : null);}

    @RolesAllowed(Position.BUYER)
    @PostMapping(value = "/vnpay-result")
    public Object onlinePaymentResult(@RequestBody VnPayResultDTO vnPayResultDTO) {
        OrderDetailDTO result = this.orderService.onlinePaymentResult(null, vnPayResultDTO);
        int code = result != null ? 200 : 400;
        return ResponseDTO.show(code, "VNPay result", code == 200 ? result : null);
    }

    /*
    * @author: Manh Tran
    * @since: 23/06/2022 1:31 CH
    * @description-VN: Lấy danh sách đơn hàng của người dùng.
    * @description-EN: Get list order of user.
    * @param: userId - Mã người dùng, pageable - Tham số phân trang.
    * @return:
    *
    * */
    @RolesAllowed(Position.BUYER)
    @GetMapping(value = "/get-order-by-user-id")
    public Object getOrderByUserId(@RequestParam(value = "userId") Long userId, Pageable pageable) {
        Page<OrderDetailDTO> result = this.orderService.getOrderByUserId(userId, pageable);
        int code = result != null ? (result.getTotalElements() > 0 ? 200 : 404) : 400;
        return ResponseDTO.show(code, "Get order by user id", code == 200 ? result : null);
    }

    /*
    * @author: Manh Tran
    * @since: 23/06/2022 1:30 CH
    * @description-VN: Lấy thông tin chi tiết đơn hàng.
    * @description-EN: Get order detail.
    * @param: orderId - Mã đơn hàng muốn lấy thông tin.
    * @return:
    *
    * */
    @RolesAllowed(Position.BUYER)
    @GetMapping(value = "/get-order-by-order-id/{orderId}")
    public Object getOrderByOrderId(@PathVariable(value = "orderId") Long orderId) {
        OrderDetailDTO result = this.orderService.getOrderByOrderId(orderId);
        int code = result != null ? 200 : 400;
        return ResponseDTO.show(code, "Get order by order id", code == 200 ? result : null);
    }

    /*
    * @author: Manh Tran
    * @since: 23/06/2022 1:30 CH
    * @description-VN: Huỷ đơn hàng khi đơn hàng chưa ở trạng thái "Đang vận chuyển".
    * @description-EN: Cancel order when order is not in shipping status.
    * @param: orderId - Mã đơn hàng muốn huỷ.
    * @return:
    *
    * */
    @RolesAllowed(Position.BUYER)
    @DeleteMapping(value = "/cancel-order")
    public Object cancelOrder(@RequestParam(value = "orderId") Long orderId) {
        int code = this.orderService.cancelOrder(orderId) ? 200 : 404;
        return ResponseDTO.show(code, "Cancel order", code == 200 ? "Success" : "Fail");
    }

    /*
    * @author: Manh Tran
    * @since: 23/06/2022 1:28 CH
    * @description-VN: Lọc đơn hàng theo trạng thái đơn hàng (của riêng người dùng).
    * @description-EN: Filter order by order status (of specific user).
    * @param: userId - id của người dùng, status - trạng thái đơn hàng.
    * @return:
    *
    * */
    @RolesAllowed(Position.BUYER)
    @GetMapping(value = "/filter-order-by-status")
    public Object filterOrderByStatus(@RequestParam(value = "userId") Long userId,
                                        @RequestParam(value = "status") String status,
                                            Pageable pageable) {
        Page<OrderDetailDTO> result = this.orderService.filterOrderByStatus(userId, status, pageable);
        int code = result != null ? (result.getTotalElements() > 0 ? 200 : 404) : 400;
        return ResponseDTO.show(code, "Filter order by status", code == 200 ? result : null);
    }

    /*
    * @author: Manh Tran
    * @since: 23/06/2022 1:28 CH
    * @description-VN: Thanh toán lại đơn hàng khi đơn hàng ở trạng thái "Chờ thanh toán".
    * @description-EN: Repay order when order is in status "Waiting for payment".
    * @param:
    * @return:
    *
    * */
    @RolesAllowed(Position.BUYER)
    @GetMapping(value = "/repay-order")
    public Object repayOrder(@RequestParam(value = "orderId") Long orderId) {
        String result = this.orderService.repayOrder(orderId);
        int code = result != null ? (!result.equals("") ? 200 : 404) : 400;
        return ResponseDTO.show(code, "Get repay link for order", code == 200 ? result : null);
    }

    /*
    * @author: Manh Tran
    * @since: 24/06/2022 8:32 SA
    * @description-VN: Lấy danh sách tất cả đơn hàng.
    * @description-EN: Get list all order.
    * @param:
    * @return:
    *
    * */

    @GetMapping(value = "/get-all-order")
    public Object getAllOrder(Pageable pageable) {
        Page<OrderDetailDTO> result = this.orderService.findAll(pageable);
        int code = result != null ? (result.getTotalElements() > 0 ? 200 : 404) : 400;
        return ResponseDTO.show(code, "Get all order", code == 200 ? result : null);
    }

    /*
    * @author: Manh Tran
    * @since: 24/06/2022 8:33 SA
    * @description-VN: Đổi trạng thái đơn hàng.
    * @description-EN: Change order status.
    * @param: orderId - mã đơn hàng.
    * @param: status - mã trạng thái đơn hàng.
    * @return:
    *
    * */
    @RolesAllowed(Position.ADMIN)
    @PutMapping(value = "/change-order-status")
    public Object changeOrderStatus(@RequestParam(value = "orderId") Long orderId,
                                    @RequestParam(value = "status") int status) {
        String result = this.orderService.changeOrderStatus(orderId, status);
        int code = result != null ? (!result.equals("") ? 200 : 404) : 400;
        return ResponseDTO.show(code, "Change order status", code == 200 ? result : null);
    }

    /*
    * @author: Manh Tran
    * @since: 28/06/2022 2:08 CH
    * @description-VN: Tìm đơn hàng theo từ khoá (nhiều tiêu chí).
    * @description-EN: Find order by keyword (multiple criteria).
    * @param: keyword - từ khoá tìm kiếm.
    * @param: pageable - tham số phân trang.
    * @return:
    *
    * */
    @GetMapping(value = "/search-order")
    public Object searchOrder(@RequestParam(value = "keyword") String keyword, Pageable pageable) {
        Page<OrderDetailDTO> result = this.orderService.searchOrder(keyword, pageable);
        int code = result != null ? (result.getTotalElements() > 0 ? 200 : 404) : 400;
        return ResponseDTO.show(code, "Search order", code == 200 ? result : null);
    }

    /*
    * @author: Manh Tran
    * @since: 28/06/2022 3:19 CH
    * @description-VN: Lọc đơn hàng theo trạng thái (gửi mã số, không gửi chuỗi).
    * @description-EN: Filter order by status (send number, not send string).
    * @param: status - mã trạng thái muốn lọc.
    * @param: pageable - tham số phân trang.
    * @return:
    *
    * */
    @GetMapping(value = "/filter-order-by-status-admin")
    public Object filterOrderByStatusAdmin(@RequestParam(value = "status") int statusId, Pageable pageable) {
        Page<OrderDetailDTO> result = this.orderService.filterOrderByStatusAdmin(statusId, pageable);
        int code = result != null ? (result.getTotalElements() > 0 ? 200 : 404) : 400;
        return ResponseDTO.show(code, "Filter order by status", code == 200 ? result : null);
    }

    /*
    * @author: Manh Tran
    * @since: 28/06/2022 3:25 CH
    * @description-VN: Lọc theo khoảng thời gian.
    * @description-EN: Filter by time range.
    * @param: startDate - thời gian bắt đầu.
    * @param: endDate - thời gian kết thúc.
    * @param: pageable - tham số phân trang.
    * @return:
    *
    * */
    @GetMapping(value = "/filter-order-by-time")
    public Object filterOrderByTime(@RequestParam(value = "startDate") String startDate,
                                    @RequestParam(value = "endDate") String endDate, Pageable pageable) {
        Page<OrderDetailDTO> result = this.orderService.filterOrderByTime(startDate, endDate, pageable);
        HttpStatus status = result != null ? (result.getTotalElements() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND) : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(ResponseDTO.show(status.value(), "Filter order by time", result));
    }

    /*
    * @author: Manh Tran
    * @since: 28/06/2022 3:28 CH
    * @description-VN: Lọc theo phương thức thanh toán.
    * @description-EN: Filter by payment method.
    * @param: paymentMethod - mã phương thức thanh toán.
    * @param: pageable - tham số phân trang.
    * @return:
    *
    * */
    @GetMapping(value = "/filter-order-by-payment-method")
    public Object filterOrderByPaymentMethod(@RequestParam(value = "paymentMethod") int paymentMethod, Pageable pageable) {
        Page<OrderDetailDTO> result = this.orderService.filterOrderByPaymentMethod(paymentMethod, pageable);
        int code = result != null ? (result.getTotalElements() > 0 ? 200 : 404) : 400;
        return ResponseDTO.show(code, "Filter order by payment method", code == 200 ? result : null);
    }

    /*
     * @author: Manh Tran
     * @since: 30/06/2022 8:13 SA
     * @description-VN: Thống kê chung về đơn hàng.
     * @description-EN: Statistic about order.
     * @param:
     * @return:
     *
     * */
    @GetMapping(value = "/statistic-order")
    public Object statisticOrder(@RequestParam(value = "startDayTime", required = false) String startDayTime,
                                 @RequestParam(value = "endDayTime", required = false) String endDayTime) {
        OrderStatisticDTO result = this.orderService.statisticOrder(startDayTime, endDayTime);
        int code = result != null ? 200 : 400;
        return ResponseDTO.show(code, "Statistic order", code == 200 ? result : null);
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
    @Operation(description = "Thống kê doanh thu")
    @GetMapping(value = "/revenue-by-hour")
    public Object statisticalRevenueByHour(@RequestParam(value = "fromDayTime", required = false) String fromDayTime,
                                           @RequestParam(value = "toDayTime", required = false) String toDayTime) {
        List<Double> result = this.orderService.statisticalByHour(fromDayTime, toDayTime, 1);
        int code = result != null ? (result.size() > 0 ? 200 : 404) : 400;
        return ResponseDTO.show(code, "Statistical revenue by hour", code == 200 ? result : null);
    }

    /*
    * @author: Manh Tran
    * @since: 30/06/2022 2:38 CH
    * @description-VN: Thống kê lợi nhuận.
    * @description-EN: Statistical profit.
    * @param:
    * @return:
    *
    * */
    @Operation(description = "Thống kê lợi nhuận")
    @GetMapping(value = "/statistic-profit")
    public Object statisticProfit(@RequestParam(value = "fromDayTime", required = false) String fromDayTime,
                                  @RequestParam(value = "toDayTime", required = false) String toDayTime) {
        List<Double> result = this.orderService.statisticalByHour(fromDayTime, toDayTime, 2);;
        int code = result != null ? (result.size() > 0 ? 200 : 404) : 400;
        return ResponseDTO.show(code, "Statistic profit", code == 200 ? result : null);
    }

    /*
    * @author: HieuMM
    * @since: 04-Jul-22 2:20 PM
    * @description-VN:  Xuất file excel cho đơn hàng.
    * @description-EN:
    * @param:
    * */
    @Autowired private IOrderRepository iOrderRepository;
    @GetMapping("/export-excel/{fromDate}/{toDate}")
    public Object exportToExcel(HttpServletResponse response, @PathVariable(value = "fromDate") java.sql.Date fromDate, @PathVariable(value = "toDate") java.sql.Date toDate) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Oder_"+fromDate+"_" + toDate + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<OrderEntity> orderEntityList = iOrderRepository.listOrderInTime(fromDate, toDate);

        BannerExcelExporterConfig excelExporter = new BannerExcelExporterConfig(orderEntityList);

        excelExporter.export(response);
        String url = "http://localhost:8080/api/v1/order/export-excel";
        url = response.encodeRedirectURL(url);
        return "redirect:" + url;
    }

}
