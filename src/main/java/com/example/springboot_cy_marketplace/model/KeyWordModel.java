package com.example.springboot_cy_marketplace.model;

import com.example.springboot_cy_marketplace.entity.KeyWordEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KeyWordModel {
    private Long id;
    private String keyWord;
    private Integer count;
    public static KeyWordModel dtoToEntity(KeyWordEntity object){
        return KeyWordModel.builder()
                .id(object.getId())
                .keyWord(object.getKeyWord())
                .count(object.getCount())
                .build();

    }
}
