package com.example.springboot_cy_marketplace.services;

import com.example.springboot_cy_marketplace.dto.*;
import com.example.springboot_cy_marketplace.entity.OrderEntity;
import com.example.springboot_cy_marketplace.model.Constant;
import com.example.springboot_cy_marketplace.model.ProductModel;
import com.example.springboot_cy_marketplace.repository.*;
import com.example.springboot_cy_marketplace.services.impl.OrderServiceImpl;
import com.example.springboot_cy_marketplace.services.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

@Service
public class AdminReportService {
    @Autowired
    OrderServiceImpl orderService;
    @Autowired
    ProductServiceImpl productService;
    @Autowired
    IProductStatisticalRepository productStatisticalRepository;
    @Autowired
    IOrderRepository orderRepository;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IReviewRepository reviewRepository;
    @Autowired
    IStatisticalRepository statisticalRepository;
    @Autowired
    IProductRepository productRepository;

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private int lastDayofMonth = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")).getMonth()
            .length(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")).isLeapYear());

    private int monthNow = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).getMonthValue();

    private int endLoop = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")).getDayOfMonth();

    private LocalDateTime start1stOfMonth = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).withDayOfMonth(1)
            .withHour(0).withMinute(0).withSecond(0);
    private LocalDateTime end1stOfMonth = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).withDayOfMonth(1)
            .withHour(23).withMinute(59).withSecond(59);
    public AdminReportDTO getAdminReport() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Saigon"));
        int dayToday = calendar.get(Calendar.DAY_OF_MONTH);
        int monthToday = calendar.get(Calendar.MONTH) + 1;
        int yearToday = calendar.get(Calendar.YEAR);

        LocalDate ld = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")).minusDays(1);
        int dayYesterday = ld.getDayOfMonth();
        int monthYesterday = ld.getMonthValue();
        int yearYesterday = ld.getYear();

        OrderStatisticDTO orderStatistic = orderService.statisticOrder(null, null);
        ProductStatisticDTO productStatistic = productService.statisticalProducts(null, null);
        AdminReportDTO adminReportDTO = new AdminReportDTO();

        // Thống kê đơn hàng từ trước đến nay
        adminReportDTO.setTotalOrderProcessing(orderStatistic.getTotalOrderProcessing());
        adminReportDTO.setTotalOrderCancel(orderStatistic.getTotalOrderCancel());
        adminReportDTO.setTotalOrderShipping(orderStatistic.getTotalOrderShipping());
        adminReportDTO.setTotalOrderDone(orderStatistic.getTotalOrderDone());
        adminReportDTO.setTotalWaitingForPayment(orderStatistic.getTotalWaitingForPayment());

        // Thống kê sản phẩm
        adminReportDTO.setTotalProducts(productStatistic.getTotalProducts());
        adminReportDTO.setTotalProductsWillBeOutOfStock(productStatistic.getTotalProductsWillBeOutOfStock());
        adminReportDTO.setTotalProductsLocked(productStatistic.getTotalProductsLocked());

        // Thống kê lượt truy cập vào trang web
        Long trafficToday = statisticalRepository.findTotalViewByDate(dayToday, monthToday, yearToday);
        adminReportDTO.setTrafficToday(trafficToday == null ? 0 : trafficToday);
        Long trafficYesterday = statisticalRepository.findTotalViewByDate(dayYesterday, monthYesterday, yearYesterday);
        adminReportDTO.setTrafficYesterday(trafficYesterday == null ? 0 : trafficYesterday);
        double calculateTraffic = ((adminReportDTO.getTrafficToday() * 1.0) / adminReportDTO.getTrafficYesterday()) * 100;
        adminReportDTO.setCaculateTraffic(String.format("%1$,.2f", calculateTraffic) + "%");

        // Thống kê lượt xem sản phẩm
        Long viewToday = productStatisticalRepository.getTotalViewsByDay(dayToday, monthToday, yearToday);
        adminReportDTO.setViewToday(viewToday == null ? 0 : viewToday);
        Long viewYesterday = productStatisticalRepository.getTotalViewsByDay(dayYesterday, monthYesterday, yearYesterday);
        adminReportDTO.setViewYesterday(viewYesterday == null ? 0 : viewYesterday);
        double calculateView = ((adminReportDTO.getViewToday() * 1.0) / adminReportDTO.getViewYesterday()) * 100;
        adminReportDTO.setCaculateView(String.format("%1$,.2f", calculateView) + "%");

        // Thống kê đơn hàng hôm nay
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayFormat = sdf.format(calendar.getTime());
        String yesterdayFormat = dtf.format(ld);
        Long newOrderToday = orderRepository.countNewOdersOfDay(todayFormat);
        adminReportDTO.setOrderToday(newOrderToday == null ? 0 : newOrderToday);
        Long newOrderYesterday = orderRepository.countNewOdersOfDay(yesterdayFormat);
        adminReportDTO.setOrderYesterday(newOrderYesterday == null ? 0 : newOrderYesterday);
        double calculateOrder = ((adminReportDTO.getOrderToday() * 1.0) / (adminReportDTO.getOrderYesterday() == 0 ? 1 : newOrderYesterday)) * 100;
        adminReportDTO.setCaculateOrder(String.format("%1$,.2f", calculateOrder) + "%");

        // Thống kê người dùng
        Long newUserToday = userRepository.countNewUsersOfDay(todayFormat);
        adminReportDTO.setUserToday(newUserToday == null ? 0 : newUserToday);
        Long newUserYesterday = userRepository.countNewUsersOfDay(yesterdayFormat);
        adminReportDTO.setUserYesterday(newUserYesterday == null ? 0 : newUserYesterday);
        double calculateUser = ((adminReportDTO.getUserToday() * 1.0) / (adminReportDTO.getUserYesterday() == 0 ? 1 : adminReportDTO.getUserYesterday())) * 100;
        adminReportDTO.setCaculateUser(String.format("%1$,.2f", calculateUser) + "%");

        // Thống kê số sao đánh giá
        double avgRate = reviewRepository.avgRate() == null ? 0 : reviewRepository.avgRate();
        adminReportDTO.setAverageRating(avgRate);
        adminReportDTO.setAverageHealth(avgRate < 4 ? Constant.RATING_BELOW_4 :
                (avgRate < 4.5 ? Constant.RATING_BETWEEN_4_45 : Constant.RATING_THAN_45));
        return adminReportDTO;
    }

    /*
     * @author: Manh Tran
     * @since: 01/07/2022 11:23 SA
     * @description-VN: Danh sách sản phẩm trên trang Admin.
     * @description-EN:
     * @param:
     * @return:
     *
     * */
    public AdminManageProductDTO findAllAdmin(Pageable page) {
        AdminManageProductDTO adminManageProduct = new AdminManageProductDTO();
        Page<ProductModel> result = productRepository.findAll(page).map(ProductModel::entityToModel);
        adminManageProduct.setProductModelPage(result);
        adminManageProduct.setTotalProduct(productRepository.count());
        adminManageProduct.setTotalProductBlocked(productRepository.findAllByEnabled(false).size());
        adminManageProduct.setTotalProductWillOutOfStock(productRepository.countProductWillBeOutOfStock());
        return adminManageProduct;
    }

    /*
     * @author: Manh Tran
     * @since: 01/07/2022 1:31 CH
     * @description-VN: Danh sách các sản phẩm sắp hết hàng.
     * @description-EN: List of products that are out of stock.
     * @param:
     * @return:
     *
     * */
    public Page<ProductModel> findAllProductsWillOutOfStock(Pageable page) {
        return productRepository.listProductWillBeOutOfStock(page).map(ProductModel::entityToModel);
    }

    /*
     * @author: Manh Tran
     * @since: 01/07/2022 2:21 CH
     * @description-VN: Thống kê doanh thu trên Admin.
     * @description-EN: Statistic revenue on Admin.
     * @param:
     * @return:
     *
     * */
    public AdminOrderDTO statisticRevenue(Pageable page, int type) {
        AdminOrderDTO adminOrderDTO = new AdminOrderDTO();
        double totalRevenue = 0;
        double totalCostPrice = 0;
        double totalProfit = 0;

        LocalDateTime ldt = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int nowMonth = ldt.getMonthValue();
        int nowYear = ldt.getYear();
        String startDate = LocalDateTime.of(nowYear, nowMonth, 1, 0, 0, 0).format(dtf);
        String endDate = ldt.format(dtf);
        Page<OrderEntity> orderEntityPage = orderRepository.adminReportRevenue(startDate, endDate, page);
        List<AdminOrderResponseDTO> orderList = this.createOrderList(orderEntityPage, type,
                0.0, 0.0, 0.0, null);
        adminOrderDTO.setTotalCostPrice(totalCostPrice);
        adminOrderDTO.setTotalRevenue(totalRevenue);
        adminOrderDTO.setTotalProfit(totalProfit);
        adminOrderDTO.setOrderList(orderList);
        return adminOrderDTO;
    }

    /*
     * @author: Manh Tran
     * @since: 01/07/2022 4:20 CH
     * @description-VN: Lọc doanh thu theo ngày.
     * @description-EN: Filter revenue by date.
     * @param:
     * @return:
     *
     * */
    public AdminOrderDTO statisticRevenueByDate(String startDate, String endDate, Pageable page) {
        AdminOrderDTO adminOrderDTO = new AdminOrderDTO();
        Double totalRevenue = 0.0;
        Double totalCostPrice = 0.0;
        Double totalProfit = 0.0;

        String startDateFormat = "";
        String endDateFormat = "";
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            startDateFormat = sdf2.format(sdf2.parse(startDate));
            startDateFormat += " 00:00:00";
            endDateFormat = sdf2.format(sdf2.parse(endDate));
            endDateFormat += " 23:59:59";
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        Page<OrderEntity> orderEntityPage = orderRepository.adminReportRevenue(startDateFormat, endDateFormat, page);
        List<AdminOrderResponseDTO> orderList = this.createOrderList(orderEntityPage, 1, totalRevenue, totalCostPrice,
                totalProfit, adminOrderDTO);
        adminOrderDTO.setOrderList(orderList);
        return adminOrderDTO;
    }

    public List<AdminOrderResponseDTO> createOrderList(Page<OrderEntity> orderEntityPage, int type, Double totalRevenue,
                                                       Double totalCostPrice, Double totalProfit, AdminOrderDTO adminOrderDTO){
        List<AdminOrderResponseDTO> orderList = new ArrayList<>();
        List<AdminOrderResponseDTO> paidOrderList = new ArrayList<>();
        List<AdminOrderResponseDTO> unPaidOrderList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        for (OrderEntity o : orderEntityPage) {
            AdminOrderResponseDTO order = new AdminOrderResponseDTO();
            order.setOrderId(o.getId());
            order.setCreateDate(sdf.format(o.getCreateDate()));
            String paymentMethod = o.getPaymentMethod();
            order.setPaymentMethod(paymentMethod);
            if (paymentMethod.equals(Constant.ORDER_PAY_PAYPAL) || paymentMethod.equals(Constant.ORDER_PAY_VNPAY)) {
                order.setStatus(Constant.ADMIN_ORDER_PAID);
                paidOrderList.add(order);
            } else if (paymentMethod.equals(Constant.ORDER_PAY_COD)) {
                if(o.getStatus().equals(Constant.ORDER_DONE)){
                    order.setStatus(Constant.ADMIN_ORDER_PAID);
                }else {
                    order.setStatus(Constant.ADMIN_ORDER_UNPAID);
                }
                unPaidOrderList.add(order);
            }
            order.setCostPrice(o.getCostPrice());
            totalCostPrice += order.getCostPrice(); // Tổng giá vốn
            order.setRevenue(o.getTotalPrice() + o.getShippingFee() - o.getTotalDiscount());
            totalRevenue += order.getRevenue(); // Tổng doanh thu
            order.setProfit(o.getTotalPrice() - o.getTotalDiscount() - o.getCostPrice());
            totalProfit += order.getProfit(); // Tổng lợi nhuận.
            orderList.add(order);
        }
        if(adminOrderDTO != null){
            adminOrderDTO.setTotalCostPrice(totalCostPrice);
            adminOrderDTO.setTotalRevenue(totalRevenue);
            adminOrderDTO.setTotalProfit(totalProfit);
        }
        if (type == 0) {
            return orderList;
        } else if (type == 1) {
            return unPaidOrderList;
        } else if (type == 2) {
            return paidOrderList;
        }
        return orderList;
    }
    /*
     * @author: Manh Tran
     * @since: 07/07/2022 3:49 CH
     * @description-VN: Doanh thu theo từng ngày trong tháng.
     * @description-EN: Revenue by each day in month.
     * @param:
     * @return:
     *
     * */
    public List<Double> revenueByDay(int month) {
        this.checkMonth(month);
        List<Double> revenueEachDay = new ArrayList<>();
        while(start1stOfMonth.getDayOfMonth() <= endLoop && start1stOfMonth.getMonthValue() == monthNow) {
            String start1stStr = start1stOfMonth.format(dtf);
            String end1stStr = end1stOfMonth.format(dtf);
            Double revenue = this.orderRepository.sumRevenueByTimeUpdate(start1stStr, end1stStr);
            revenueEachDay.add(revenue == null ? 0 : revenue);
            start1stOfMonth = start1stOfMonth.plusDays(1);
            end1stOfMonth = end1stOfMonth.plusDays(1);
        }
        return revenueEachDay;
    }

    /*
     * @author: Manh Tran
     * @since: 07/07/2022 3:50 CH
     * @description-VN: Lợi nhuận theo từng ngày trong tháng.
     * @description-EN: Profit by each day in month.
     * @param:
     * @return:
     *
     * */
    public List<Double> profitByDay(int month) {
        this.checkMonth(month);
        List<Double> revenueEachDay = new ArrayList<>();
        while(start1stOfMonth.getDayOfMonth() <= endLoop && start1stOfMonth.getMonthValue() == monthNow){
            String start1stStr = start1stOfMonth.format(dtf);
            String end1stStr = end1stOfMonth.format(dtf);
            Double revenue = this.orderRepository.sumProfitByTimeUpdate(start1stStr, end1stStr);
            revenueEachDay.add(revenue == null ? 0 : revenue);
            start1stOfMonth = start1stOfMonth.plusDays(1);
            end1stOfMonth = end1stOfMonth.plusDays(1);
        }
        return revenueEachDay;
    }

    private void checkMonth(int month){
        if(month != 0 && month != monthNow) {
            lastDayofMonth = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")).withMonth(month).lengthOfMonth();
            start1stOfMonth = start1stOfMonth.withMonth(month);
            end1stOfMonth = end1stOfMonth.withMonth(month);
            endLoop = lastDayofMonth;
        }
        start1stOfMonth = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0);
        end1stOfMonth = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).withDayOfMonth(1)
                .withHour(23).withMinute(59).withSecond(59);
    }
}
