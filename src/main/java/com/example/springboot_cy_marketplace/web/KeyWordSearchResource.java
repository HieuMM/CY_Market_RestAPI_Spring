package com.example.springboot_cy_marketplace.web;

import com.example.springboot_cy_marketplace.dto.KeyWordDTO;
import com.example.springboot_cy_marketplace.dto.ResponseDTO;
import com.example.springboot_cy_marketplace.entity.KeyWordEntity;
import com.example.springboot_cy_marketplace.model.KeyWordModel;
import com.example.springboot_cy_marketplace.repository.IKeyWordRepository;
import com.example.springboot_cy_marketplace.services.impl.KeyWordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/keyword")
public class KeyWordSearchResource {
    @Autowired
    KeyWordServiceImpl keyWordService;
    @Autowired
    IKeyWordRepository keyWordRepository;
/*
* @author: HieuMM
* @since: 28-Jun-22 2:38 PM
* @description-VN:  Lấy keyword tìm kiếm gửi về database
* @description-EN:
* @param:
*
* */
    @PostMapping(value = "/add")
    public Object addReview(KeyWordDTO keyWordDTO) {
        KeyWordModel model = keyWordService.sendKeyWordToModel(keyWordDTO);
        List<String> wordEntityList = keyWordRepository.findAllKeyWord();
        if (wordEntityList.contains(keyWordDTO.getKeyWord())) {//nếu keyword đã tồn tại trong database thì thêm lượt đếm
            KeyWordEntity keyWordEntity = keyWordRepository.findByKeyWord(keyWordDTO.getKeyWord());
            keyWordEntity.setCount(keyWordEntity.getCount() + 1);
            keyWordRepository.save(keyWordEntity);
           /* KeyWordEntity keyWordEntity = keyWordService.updateCount(model);*/
            return ResponseDTO.of(KeyWordDTO.dtoToEntity(keyWordEntity), "Update keyword");
        }else {//nếu keyword chưa tồn tại trong database thì thêm keyword
            KeyWordEntity keyWordEntity = keyWordService.add(model);
            if (keyWordEntity == null) {
                return ResponseDTO.of(null, "Add keyword fail");
            } else {
                return ResponseDTO.of(KeyWordDTO.dtoToEntity(keyWordEntity), "Add keyword");
            }
        }
    }
    /*
    * @author: HieuMM
    * @since:  2:40 PM
    * @description-VN:  HIỂN THỊ KEYWORD SẮP XẾP THEO LƯỢNG TÌM KIẾM
    * @description-EN:
    * @param:
    * */
    @GetMapping("/findAllKeyWord")
    public Object findAllReview(Pageable pageable) {
        return ResponseDTO.of(keyWordService.findAllKeyWordTo(pageable), "findAllKeyWord");
    }
}
