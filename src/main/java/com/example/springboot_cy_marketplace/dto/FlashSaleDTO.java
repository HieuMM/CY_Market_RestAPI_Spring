package com.example.springboot_cy_marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FlashSaleDTO {
    private Long id;
    private String name;
    private String description;
    private String startDate;
    private Long startDateConvert;
    private String endDate;
    private Long endDateConvert;
    private String status;
    private List<FlashSaleItemDTO> items;
}
