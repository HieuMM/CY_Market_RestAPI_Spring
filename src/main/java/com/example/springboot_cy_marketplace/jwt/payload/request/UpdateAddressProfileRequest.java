package com.example.springboot_cy_marketplace.jwt.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
public class UpdateAddressProfileRequest {
    private Long id;
    private Long cityId;
    private Long districtId;
    private Long wardId;
    private String address;
}
