package com.example.springboot_cy_marketplace.model;

import com.example.springboot_cy_marketplace.entity.DistrictEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class WardModel {
    private Long id;
    private String name;
    private String type;
    private DistrictEntity district;
}
