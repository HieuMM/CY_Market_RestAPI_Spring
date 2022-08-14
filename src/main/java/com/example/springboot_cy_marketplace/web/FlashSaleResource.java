package com.example.springboot_cy_marketplace.web;

import com.example.springboot_cy_marketplace.dto.FlashSaleDTO;
import com.example.springboot_cy_marketplace.dto.ResponseDTO;
import com.example.springboot_cy_marketplace.services.FlashSaleService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/flash-sale")
public class FlashSaleResource {
    @Autowired
    FlashSaleService flashSaleService;

    /*
     * @author: Manh Tran
     * @since: 06/07/2022 10:10 SA
     * @description-VN: Thêm mới Flash Sale.
     * @description-EN: Add new Flash Sale.
     * @param: flashSaleDTO - Thông tin Flash Sale.
     * @return:
     *
     * */
    @Operation(summary = "Thêm chương trình Flash Sale.")
    @PostMapping(value = "/add")
    public Object addFlashSale(@RequestBody FlashSaleDTO flashSaleDTO) {
        Object result = flashSaleService.addNewFlashSale(flashSaleDTO);
        HttpStatus status = result != null ? (result instanceof String ? HttpStatus.BAD_REQUEST : HttpStatus.OK) : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(ResponseDTO.show(status.value(), "Add new flash sale", result));
    }

    /*
     * @author: Manh Tran
     * @since: 06/07/2022 10:11 SA
     * @description-VN: Lấy danh sách Flash Sale.
     * @description-EN: Get list Flash Sale.
     * @param: pageable - Thông tin phân trang.
     * @param: type - 1 là trang chủ, 2 là trang Admin.
     * @return:
     *
     * */
    @Operation(summary = "Danh sách chương trình Flash Sale, type = 1 là trang chủ, type = 2 là trang Admin.")
    @GetMapping(value = "/list")
    public Object findAllFlashSale(@RequestParam(value = "type") int type,
                                   @PageableDefault(sort = {"startDate"},
                                    direction = Sort.Direction.ASC, size = 100) Pageable pageable){
        Object result = flashSaleService.findAllFlashSale(type, pageable);
        HttpStatus status = result != null ? (result instanceof String ? HttpStatus.BAD_REQUEST : HttpStatus.OK) : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(ResponseDTO.show(status.value(), "List flash sale", result));
    }

    /*
    * @author: Manh Tran
    * @since: 07/07/2022 8:26 SA
    * @description-VN: Chi tiết chương trình Flash Sale.
    * @description-EN: Flash Sale detail.
    * @param: id - Id chương trình Flash Sale.
    * @return:
    *
    * */
    @Operation(summary = "Chi tiết chương trình Flash Sale.")
    @GetMapping(value = "/detail")
    public Object findFlashSaleDetail(@RequestParam(value = "id") long id){
        Object result = flashSaleService.findFlashSaleDetail(id);
        HttpStatus status = result != null ? (result instanceof String ? HttpStatus.BAD_REQUEST : HttpStatus.OK) : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(ResponseDTO.show(status.value(), "Get flash sale detail", result));
    }
    /*
     * @author: Manh Tran
     * @since: 06/07/2022 11:14 SA
     * @description-VN: Cập nhật Flash Sale.
     * @description-EN: Update Flash Sale.
     * @param:
     * @return:
     *
     * */
    @Operation(summary = "Cập nhật chương trình Flash Sale.")
    @PutMapping(value = "/update")
    public Object updateFlashSale(@RequestBody FlashSaleDTO flashSaleDTO){
        Object result = flashSaleService.update(flashSaleDTO);
        HttpStatus status = result != null ? (result instanceof String ? HttpStatus.BAD_REQUEST : HttpStatus.OK) : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(ResponseDTO.show(status.value(), "Update flash sale", result));
    }

    /*
     * @author: Manh Tran
     * @since: 06/07/2022 1:46 CH
     * @description-VN: Xoá Flash Sale.
     * @description-EN: Delete Flash Sale.
     * @param:
     * @return:
     *
     * */
    @Operation(summary = "Xoá chương trình Flash Sale.")
    @DeleteMapping(value = "/delete")
    public Object deleteFlashSale(@RequestParam(value = "id") Long id){
        Object result = flashSaleService.delete(id);
        HttpStatus status = result != null ? (result instanceof String ? HttpStatus.BAD_REQUEST : HttpStatus.OK) : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(ResponseDTO.show(status.value(), "Delete flash sale", result));
    }

    /*
     * @author: Manh Tran
     * @since: 06/07/2022 2:03 CH
     * @description-VN: Tìm kiếm Flash Sale.
     * @description-EN: Find Flash Sale.
     * @param:
     * @return:
     *
     * */
    @Operation(summary = "Tìm kiếm chương trình Flash Sale theo từ khoá, khoảng thời gian.")
    @GetMapping(value = "/search")
    public Object findFlashSale(@RequestParam(value = "keyword", required = false) String keyword,
                                @RequestParam(value = "startDate", required = false) String startDate,
                                @RequestParam(value = "endDate", required = false) String endDate,
                                @PageableDefault(sort = {"startDate"},
                                        direction = Sort.Direction.ASC, size = 30) Pageable pageable){
        Object result = flashSaleService.find(keyword, startDate, endDate, pageable);
        HttpStatus status = result != null ? (result instanceof String ? HttpStatus.BAD_REQUEST : HttpStatus.OK) :
                HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(ResponseDTO.show(status.value(), "Search flash sale", result));
    }

    /*
    * @author: Manh Tran
    * @since: 07/07/2022 7:41 SA
    * @description-VN: Bắt đầu và kết thúc (nếu có) Flash Sale.
    * @description-EN: Start and end (if there is) Flash Sale.
    * @param: hourStart - khung giờ bắt đầu.
    * @return:
    *
    * */
    @Operation(summary = "Bắt đầu chạy chương trình Flash Sale (CẬP NHẬT).")
    @PutMapping(value = "/start")
    public Object startFlashSale(@RequestParam(value = "nowFlashSaleId") Long nowFlashSaleId,
                                    @RequestParam(value = "nextFlashSaleId", required = false) Long nextFlashSaleId){
        Object result = flashSaleService.startFlashSaleById(nowFlashSaleId, nextFlashSaleId);
        HttpStatus status = result != null ? (result instanceof String ? HttpStatus.BAD_REQUEST : HttpStatus.OK) : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(ResponseDTO.show(status.value(), "Start flash sale", result));
    }
}
