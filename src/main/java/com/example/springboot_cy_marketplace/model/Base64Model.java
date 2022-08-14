package com.example.springboot_cy_marketplace.model;

import com.amazonaws.services.elasticmapreduce.model.ScalingTrigger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Base64Model {
    private String fileName;
    private String data;
}
