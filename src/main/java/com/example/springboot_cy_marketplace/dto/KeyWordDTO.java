package com.example.springboot_cy_marketplace.dto;

import com.example.springboot_cy_marketplace.entity.KeyWordEntity;
import com.example.springboot_cy_marketplace.model.KeyWordModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class KeyWordDTO {
    private Long id;
    private String keyWord;
    public static KeyWordModel dtoToEntity(KeyWordEntity object){
        return KeyWordModel.builder()
                .id(object.getId())
                .keyWord(object.getKeyWord())
                .count(object.getCount())
                .build();

    }
}
