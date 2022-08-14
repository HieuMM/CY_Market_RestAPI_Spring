package com.example.springboot_cy_marketplace.model;

import com.example.springboot_cy_marketplace.entity.AddressEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AddressModel {
    private Long id;
    private String city;
    private String district;
    private String ward;
    private String address;
    private Long cityGhnID;
    private Long districtGhnID;
    private boolean defaultAdd;

    public static AddressModel entityToModel(AddressEntity object){
        StringBuilder sb = new StringBuilder();

        return AddressModel.builder()
                .id(object.getId())
                .city(object.getCity())
                .district(object.getDistrict())
                .ward(object.getWard())
                .address(sb.append(object.getAddress()).append(", ").append(object.getWard()).append(", ")
                        .append(object.getDistrict()).append(", ").append(object.getCity()).toString())
                .cityGhnID(object.getIdCityGHN())
                .districtGhnID(object.getIdDistrictGHN())
                .defaultAdd(object.getDefaultAdd())
                .build();
    }
}
