package com.example.springboot_cy_marketplace.web;

import com.example.springboot_cy_marketplace.dto.*;
import com.example.springboot_cy_marketplace.model.ProductModel;
import com.example.springboot_cy_marketplace.services.AdminReportService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.TimeZone;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminReportResource {
    @Autowired
    AdminReportService adminReportService;

    /*
     * @author: Manh Tran
     * @since: 01/07/2022 11:26 SA
     * @description-VN: Lấy tất cả dữ liệu thống kê ở trang chủ Admin.
     * @description-EN:
     * @param:
     * @return:
     *
     * */
    @Operation(summary = "Lấy tất cả dữ liệu thống kê ở trang chủ Admin")
    @GetMapping(value = "/report")
    public Object getAdminReport() {
        AdminReportDTO result = adminReportService.getAdminReport();
        HttpStatus statusResponse = result != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(statusResponse).body(ResponseDTO.show(statusResponse.value(),
                "Get admin report", result));
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
    @Operation(summary = "Danh sách sản phẩm trên trang Admin.")
    @GetMapping("/all-products")
    public Object findAllProductsAdmin(Pageable pageable) {
        AdminManageProductDTO result = adminReportService.findAllAdmin(pageable);
        HttpStatus statusResponse = result != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(statusResponse).body(ResponseDTO.show(statusResponse.value(),
                "Find all product in admin page", result));
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
    @Operation(summary = "Danh sách các sản phẩm sắp hết hàng.")
    @GetMapping("/product-will-out-of-stock")
    public Object findAllProductsOutOfStock(Pageable pageable) {
        Page<ProductModel> result = adminReportService.findAllProductsWillOutOfStock(pageable);
        HttpStatus statusResponse = result != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(statusResponse).body(ResponseDTO.show(statusResponse.value(),
                "Get products will out of stock", result));
    }

    /*
     * @author: Manh Tran
     * @since: 01/07/2022 2:21 CH
     * @description-VN: Thống kê doanh thu trên Admin.
     * @description-EN: Statistic revenue on Admin.
     * @param: pageable - Tham số phân trang.
     * @return:
     *
     * */
    @Operation(summary = "Thống kê doanh thu trên Admin.")
    @GetMapping("/revenue")
    public Object getRevenue(Pageable pageable) {
        AdminOrderDTO result = adminReportService.statisticRevenue(pageable, 0);
        HttpStatus statusResponse = result != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(statusResponse).body(ResponseDTO.show(statusResponse.value(),
                "Get revenue", result));
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
    @Operation(summary = "Lọc doanh thu theo ngày.")
    @GetMapping("/revenue-by-date")
    public Object getRevenueByDate(@RequestParam(value = "start", required = true) String start,
                                   @RequestParam(value = "end", required = false) String end,
                                   @PageableDefault(value = 500) Pageable pageable) {
        AdminOrderDTO result = adminReportService.statisticRevenueByDate(start, end, pageable);
        HttpStatus statusResponse = result != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(statusResponse).body(ResponseDTO.show(statusResponse.value(),
                "Get revenue by date", result));
    }

    /*
     * @author: Manh Tran
     * @since: 04/07/2022 9:19 SA
     * @description-VN: Lọc đơn hàng theo trạng thái (1 - Sẽ thanh toán, 2 - Đã thanh toán).
     * @description-EN: Filter order by status (1 - Will pay, 2 - Paid).
     * @param: status - Mã trạng thái.
     * @param: page - Thông số phân trang.
     * @return:
     *
     * */
    @Operation(summary = "Lọc doanh thu, lấy các đơn hàng theo trạng thái (1 - Sẽ thanh toán, 2 - Đã thanh toán).")
    @GetMapping(value = "/filter-order-by-status")
    public Object filterOrderByStatus(@RequestParam(value = "status", required = true) int status,
                                      @PageableDefault(size = 40) Pageable pageable) {
        AdminOrderDTO result = this.adminReportService.statisticRevenue(pageable, status);
        HttpStatus statusResponse = result != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(statusResponse).body(ResponseDTO.show(statusResponse.value(),
                "Filter order by status", result));
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
    @GetMapping("/revenue-by-day")
    public Object revenueByDay(@RequestParam(value = "month", required = false, defaultValue = "0") int month){
        List<Double> result = adminReportService.revenueByDay(month);
        HttpStatus status = result.size() > 0 ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(ResponseDTO.show(status.value(), "Get revenue by day " +
                        LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")) + ", ngày hiện tại: " +
                        LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")).getDayOfMonth(), result));
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
    @GetMapping("/profit-by-day")
    public Object profitByDay(@RequestParam(value = "month", required = false, defaultValue = "0") int month){
        List<Double> result = adminReportService.profitByDay(month);
        HttpStatus status = result.size() > 0 ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(ResponseDTO.show(status.value(), "Get profit by day " +
                LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")) + ", ngày hiện tại: " +
                        LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")).getDayOfMonth(), result));
    }
}
