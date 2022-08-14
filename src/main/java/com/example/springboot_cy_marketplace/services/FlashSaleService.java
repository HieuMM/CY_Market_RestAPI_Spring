package com.example.springboot_cy_marketplace.services;

import com.example.springboot_cy_marketplace.dto.FlashSaleDTO;
import com.example.springboot_cy_marketplace.dto.FlashSaleItemDTO;
import com.example.springboot_cy_marketplace.entity.*;
import com.example.springboot_cy_marketplace.model.Constant;
import com.example.springboot_cy_marketplace.model.ProductCategoryModel;
import com.example.springboot_cy_marketplace.repository.IFlashSaleItemRepository;
import com.example.springboot_cy_marketplace.repository.IFlashSaleRepository;
import com.example.springboot_cy_marketplace.repository.IProductClassifiedRepository;
import com.example.springboot_cy_marketplace.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class FlashSaleService {
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Autowired
    IFlashSaleRepository flashSaleRepository;
    @Autowired
    IProductClassifiedRepository productClassifiedRepository;
    @Autowired
    IProductRepository productRepository;
    @Autowired
    IFlashSaleItemRepository flashSaleItemRepository;
    private LocalDateTime startDate = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")),
            endDate = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));

    private FlashSaleEntity currentFlashSale = new FlashSaleEntity();

    /*
     * @author: Manh Tran
     * @since: 06/07/2022 10:10 SA
     * @description-VN: Thêm mới Flash Sale.
     * @description-EN: Add new Flash Sale.
     * @param: flashSaleDTO - Thông tin Flash Sale.
     * @return:
     *
     * */
    public Object addNewFlashSale(FlashSaleDTO flashSaleDTO) {
        try {
            startDate = LocalDateTime.parse(flashSaleDTO.getStartDate(), dtf);
            if (startDate.isBefore(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")))) {
                return "Thời gian bắt đầu khuyến mãi cần lớn hơn thời gian hiện tại.";
            }
            endDate = LocalDateTime.parse(flashSaleDTO.getEndDate(), dtf);
            if (endDate.isBefore(startDate)) {
                return "Thời gian kết thúc khuyến mãi cần lớn hơn thời gian bắt đầu.";
            }
            Duration duration = Duration.between(startDate, endDate);
            Period period = Period.between(startDate.toLocalDate(), endDate.toLocalDate());
            if (duration.getSeconds() / 60 < 10) {
                return "Thời gian khuyến mãi phải tối thiểu 10 phút.";
            }
        } catch (DateTimeParseException ex) {
            ex.printStackTrace();
            return "Định dạng ngày tháng không hợp lệ";
        }

        FlashSaleEntity entity = this.dtoToEntity(flashSaleDTO);
        if (entity == null) {
            return null;
        }

        if (flashSaleDTO.getId() != null) {
//            boolean isExist = flashSaleRepository.existsByStartDate(startDate);
//            if (isExist) {
//                return "Chương trình khuyến mãi đã tồn tại.";
//            }
            entity.setId(flashSaleDTO.getId());
        }

        // Chuyển về định dạng thời gian trên máy chủ
        try{
            ZoneId systemZone = ZoneId.systemDefault();
            startDate = startDate.atZone(systemZone).toLocalDateTime();
            endDate = endDate.atZone(systemZone).toLocalDateTime();
        }catch (Exception ex){
            return "Lỗi chuyển định dạng thời gian trên máy chủ.";
        }
        Page<FlashSaleEntity> isExist = flashSaleRepository.findByDate(startDate, endDate, PageRequest.of(0, 1));
        if (isExist.getTotalElements() > 0) {
            return "Đã có chương trình khuyến mãi trong khung giờ này.";
        }
        FlashSaleEntity result = flashSaleRepository.save(entity);
        
        Pageable pageable = PageRequest.of(0, 2, Sort.by("startDate").ascending());
        Page<FlashSaleDTO> allFlashSale = (Page<FlashSaleDTO>) this.findAllFlashSale(1, pageable);
        List<FlashSaleDTO> content = allFlashSale.getContent();
        if(content.get(0).getId() == result.getId()){
            this.updateProductPrice(result);
        }
        return this.entityToDto(result);
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
    public Object findAllFlashSale(int type, Pageable pageable) {
        Page<FlashSaleEntity> result = null;
        if (type == 1) {
            result = flashSaleRepository.findByEndDateAfter(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")), pageable);
        } else {
            Pageable pageable1 = PageRequest.of(0, 100, Sort.by("modifiedDate").descending());
            result = flashSaleRepository.findAll(pageable1);
        }
        if (result.getTotalElements() == 0) {
            return "Hiện tại không có chương trình khuyến mãi nào.";
        }
        return result.map(this::entityToDto);
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
    public Object update(FlashSaleDTO flashSaleDTO) {
        if (flashSaleDTO.getId() == null) {
            return "Bắt buộc phải có mã chương trình khuyến mãi.";
        }
        FlashSaleEntity entity = flashSaleRepository.findById(flashSaleDTO.getId()).orElse(null);
        if (entity == null) {
            return "Chương trình khuyến mãi không tồn tại.";
        }
        currentFlashSale = entity;
        Object result = this.addNewFlashSale(flashSaleDTO);
        return result;
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
    public Object delete(Long id) {
        if (id == null) {
            return "Bắt buộc phải có mã chương trình khuyến mãi.";
        }

        FlashSaleEntity entity = flashSaleRepository.findById(id).orElse(null);
        if (entity == null) {
            return "Chương trình khuyến mãi không tồn tại.";
        }
        this.flashSaleRepository.delete(entity);
        return this.entityToDto(entity);
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
    public Object find(String keyword, String startDate, String endDate, Pageable pageable) {
        Page<FlashSaleEntity> result = new PageImpl<>(new ArrayList<>());
        if (keyword != null) {
            result = this.flashSaleRepository.findByKeyword(keyword, pageable);
        } else {
            DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startDateTime = null, endDateTime = null;
            if (startDate == null) {
                startDateTime = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
            } else {
                try {
                    startDateTime = LocalDateTime.parse(startDate, dtf2);
                } catch (DateTimeParseException ex) {
                    return "Định dạng ngày tháng không hợp lệ.";
                }
            }

            if (endDate == null) {
                endDateTime = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
            } else {
                try {
                    endDateTime = LocalDateTime.parse(endDate, dtf2);
                } catch (DateTimeParseException ex) {
                    return "Định dạng ngày tháng không hợp lệ.";
                }
            }

            result = this.flashSaleRepository.findByDate(startDateTime, endDateTime, pageable);
        }
        if (result.getTotalElements() == 0) {
            return "Không tìm thấy chương trình khuyến mãi nào.";
        }
        return result.map(this::entityToDto);
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
    public Object findFlashSaleDetail(Long id) {
        if (id == null) {
            return "Bắt buộc phải có mã chương trình khuyến mãi.";
        }
        FlashSaleEntity entity = flashSaleRepository.findById(id).orElse(null);
        if (entity == null) {
            return "Không tìm thấy chương trình khuyến mãi nào.";
        }
        return this.entityToDto(entity);
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
    public Object startFlashSale(int hourStart) {
        if (hourStart < 0 || hourStart > 22 || hourStart < LocalDateTime.now().getHour()) {
            return "Khung giờ không hợp lệ.";
        }
        FlashSaleEntity previousFlashSale = new FlashSaleEntity();
        FlashSaleEntity nextFlashSale = new FlashSaleEntity();
        List<FlashSaleEntity> allFlashSaleInDay = flashSaleRepository.findAllFlashSaleInDay(startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Collections.sort(allFlashSaleInDay, (o1, o2) -> {
            if (o1.getStartDate().isBefore(o2.getStartDate())) {
                return -1;
            } else if (o1.getStartDate().isAfter(o2.getStartDate())) {
                return 1;
            } else {
                return 0;
            }
        });
        List<FlashSaleEntity> allFlashSaleYesterday = flashSaleRepository.findAllFlashSaleInDay(startDate.minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Collections.sort(allFlashSaleYesterday, (o1, o2) -> {
            if (o1.getStartDate().isBefore(o2.getStartDate())) {
                return -1;
            } else if (o1.getStartDate().isAfter(o2.getStartDate())) {
                return 1;
            } else {
                return 0;
            }
        });
        String convertStartHour = startDate.withHour(hourStart).withMinute(0).format(DateTimeFormatter.ofPattern("HH:mm"));
        for (int i = 0; i < allFlashSaleInDay.size(); i++) {
            if (allFlashSaleInDay.get(i).getStartDate().toString().contains(convertStartHour)) {
                nextFlashSale = allFlashSaleInDay.get(i);
                if (i != 0) {
                    previousFlashSale = allFlashSaleInDay.get(i - 1);
                } else if (i == 0) {
                    previousFlashSale = allFlashSaleYesterday.get(allFlashSaleYesterday.size() - 1);
                }
            }
        }
        if (nextFlashSale == null) {
            return "Không tìm thấy chương trình khuyến mãi nào ở khung giờ này.";
        }
        if (previousFlashSale != null) {
            this.restoreProductPrice(previousFlashSale);
        }
        this.updateProductPrice(nextFlashSale);
        return this.entityToDto(nextFlashSale);
    }

    public Object startFlashSaleById(Long nowFlashSaleId, Long nextFlashSaleId){
        if(nowFlashSaleId == null){
            return "Bắt buộc phải có mã chương trình khuyến mãi hiện tại để cập nhật giá.";
        }

        FlashSaleEntity nowFlashSale = flashSaleRepository.findById(nowFlashSaleId).orElse(null);
        if (nowFlashSale == null) {
            return "Không tìm thấy chương trình khuyến mãi hiện tại.";
        }

        if(nextFlashSaleId == null){
            this.restoreProductPrice(nowFlashSale);
            return this.entityToDto(nowFlashSale);
        }

        FlashSaleEntity nextFlashSale = flashSaleRepository.findById(nextFlashSaleId).orElse(null);
        if (nextFlashSale == null) {
            return "Không tìm thấy chương trình khuyến mãi sắp tới";
        }
        this.restoreProductPrice(nowFlashSale);
        this.updateProductPrice(nextFlashSale);
        return this.entityToDto(nextFlashSale);
    }
    public void updateProductPrice(FlashSaleEntity nextFlashSale) {
        Set<FlashSaleItemEntity> items = nextFlashSale.getItems();
        for (FlashSaleItemEntity item : items) {
            ProductClassifiedEntity product = item.getProductClassified();
            product.setNewPrice((long) item.getFlashSalePrice() + "");
            product.setAmount(item.getQuantitySales());
            int discount = 100 - (int)(item.getFlashSalePrice() * 100 / Long.parseLong(product.getOldPrice()));
            product.setDiscount(discount);
            product.getProductEntity().setFlashSale(true);
            //this.flashSaleItemRepository.updateNewPrice(product.getNewPrice(), product.getId());
            this.productClassifiedRepository.save(product);
        }
    }

    public void restoreProductPrice(FlashSaleEntity previousFlashSale) {
        Set<FlashSaleItemEntity> items = previousFlashSale.getItems();
        for (FlashSaleItemEntity item : items) {
            ProductClassifiedEntity product = item.getProductClassified();
            product.setNewPrice(item.getCurrentPrice() + "");
            int discount = 100 - (int)(item.getCurrentPrice() * 100 / Long.parseLong(product.getOldPrice()));
            product.setDiscount(discount);
            product.setAmount(item.getCurrentStock() - (item.getQuantitySales() - product.getAmount()));
            product.getProductEntity().setFlashSale(false);
            this.productClassifiedRepository.save(product);
        }
    }

    public FlashSaleEntity dtoToEntity(FlashSaleDTO dto) {
        Set<FlashSaleItemEntity> items = new HashSet<>();
        for (FlashSaleItemDTO item : dto.getItems()) {
            List<FlashSaleItemEntity> itemEntityList = this.createItemEntityList(item);
            if (itemEntityList == null) {
                return null;
            }
            items.addAll(itemEntityList);
        }
        if (dto.getId() != null) {
            Set<FlashSaleItemEntity> oldItems = currentFlashSale.getItems();
            for (FlashSaleItemEntity item : oldItems) {
                this.flashSaleItemRepository.deleteByIdCustom(item.getId());
            }
        }
        return new FlashSaleEntity(dto.getName(), dto.getDescription(), startDate, endDate, items);
    }

    public FlashSaleDTO entityToDto(FlashSaleEntity entity) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        FlashSaleDTO dto = new FlashSaleDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setStartDate(entity.getStartDate().format(dtf));
        // Chuyển ngày tháng về Long
        ZonedDateTime zdtStart = ZonedDateTime.of(entity.getStartDate(), ZoneId.of("Asia/Ho_Chi_Minh"));
        dto.setStartDateConvert(zdtStart.toInstant().toEpochMilli());
        dto.setEndDate(entity.getEndDate().format(dtf));
        ZonedDateTime zdtEnd = ZonedDateTime.of(entity.getEndDate(), ZoneId.of("Asia/Ho_Chi_Minh"));
        dto.setEndDateConvert(zdtEnd.toInstant().toEpochMilli());
        if (now.isAfter(entity.getStartDate()) && now.isBefore(entity.getEndDate())) {
            dto.setStatus(Constant.FLASH_SALE_STATUS_ACTIVE);
        } else if (now.isBefore(entity.getStartDate())) {
            dto.setStatus(Constant.FLASH_SALE_STATUS_WAITING);
        } else if (now.isAfter(entity.getEndDate())) {
            dto.setStatus(Constant.FLASH_SALE_STATUS_INACTIVE);
        }

        List<FlashSaleItemDTO> items = new ArrayList<>();
        Set<ProductEntity> productEntities = new HashSet<>();
        for (FlashSaleItemEntity item : entity.getItems()) {
            ProductEntity productEntity = item.getProductClassified().getProductEntity();
            boolean result = productEntities.add(productEntity);
            if (result) {
                items.add(this.createItemDto(item, productEntity));
            }
        }
        dto.setItems(items);
        return dto;
    }

    public FlashSaleItemDTO createItemDto(FlashSaleItemEntity item, ProductEntity productEntity) {
        if (item.getProductClassified() == null) {
            return new FlashSaleItemDTO(item.getId(), item.getFlashSalePercent(),
                    item.getFlashSalePrice(), item.getQuantitySales(), item.getLimitPerCustomer());
        }
        double discount = Double.valueOf(item.getProductClassified().getNewPrice()) / item.getFlashSalePrice();
        ProductCategoryEntity parentCategory = productEntity.getProductCategoryEntity().getParentCategory();
        ProductCategoryModel productCategoryModel = new ProductCategoryModel();
        productCategoryModel.setId(productEntity.getProductCategoryEntity().getId());
        productCategoryModel.setName(productEntity.getProductCategoryEntity().getName());
        productCategoryModel.setAvatarUrl(productEntity.getProductCategoryEntity().getAvatarUrl());
        productCategoryModel.setParentId(parentCategory == null ? null : parentCategory.getId());
        productCategoryModel.setParentName(parentCategory == null ? null : parentCategory.getName());
        productCategoryModel.setParentAvatarUrl(parentCategory == null ? null : parentCategory.getAvatarUrl());
        return new FlashSaleItemDTO(item.getId(), item.getFlashSalePercent(),
                item.getFlashSalePrice(), item.getQuantitySales(),
                item.getLimitPerCustomer(), productEntity.getId(),
                productEntity.getName(),
                productEntity.getCoverImage(), productEntity.getMinNewPrice(),
                (int)item.getCurrentPrice(), productEntity.getMinOldPrice(),
                productEntity.getMaxOldPrice(), productEntity.getTotalAmount(),
                productEntity.getProductCategoryEntity().getId(),
                productCategoryModel,
                productEntity.getStatus(), productEntity.isEnabled());
    }

    public List<FlashSaleItemEntity> createItemEntityList(FlashSaleItemDTO item) {
        ProductEntity productEntity = productRepository.findById(item.getProductId()).orElse(null);
        if (productEntity == null) {
            return null;
        }
        List<FlashSaleItemEntity> items = new ArrayList<>();
        List<ProductClassifiedEntity> productClassifiedEntityList = productEntity.getProductClassifiedEntityList();
        if (productClassifiedEntityList.size() == 0) {
            return null;
        }
        for (ProductClassifiedEntity entity : productClassifiedEntityList) {
            double flashSalesPrice = Double.parseDouble(entity.getOldPrice()) * (100 - item.getFlashSalePercent()) / 100;
            FlashSaleItemEntity newFlashSaleItem = new FlashSaleItemEntity(Double.parseDouble(entity.getNewPrice()),
                    flashSalesPrice, item.getFlashSalePercent(), item.getQuantitySales(), item.getLimitPerCustomer(), entity.getAmount(),
                    entity);
            items.add(newFlashSaleItem);
        }
        return items;
    }
}
