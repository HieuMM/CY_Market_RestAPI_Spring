package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.dto.KeyWordDTO;
import com.example.springboot_cy_marketplace.entity.KeyWordEntity;
import com.example.springboot_cy_marketplace.model.KeyWordModel;
import com.example.springboot_cy_marketplace.repository.IKeyWordRepository;
import com.example.springboot_cy_marketplace.services.IKeyWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeyWordServiceImpl implements IKeyWordService {
    @Autowired
    IKeyWordRepository keyWordRepository;

    @Override
    public List<KeyWordModel> findAll() {
        return null;
    }

    @Override
    public Page<KeyWordModel> findAll(Pageable page) {
        return null;
    }

    @Override
    public KeyWordModel findById(Long id) {
        return null;
    }

    @Override
    public KeyWordModel add(KeyWordDTO dto) {
        return null;
    }

    @Override
    public List<KeyWordModel> add(List<KeyWordDTO> dto) {
        return null;
    }

    @Override
    public KeyWordModel update(KeyWordDTO dto) {
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public boolean deleteByIds(List<Long> id) {
        return false;
    }

    @Override
    public KeyWordEntity add(KeyWordModel model) {
        KeyWordEntity keyWordEntity = toEntity(model);
        return keyWordRepository.save(keyWordEntity);
    }

    private KeyWordEntity toEntity(KeyWordModel model) {
        KeyWordEntity keyWordEntity = new KeyWordEntity();
        keyWordEntity.setId(model.getId());
        keyWordEntity.setKeyWord(model.getKeyWord());
        keyWordEntity.setCount(model.getCount());
        return keyWordRepository.save(keyWordEntity);
    }

    public KeyWordModel sendKeyWordToModel(KeyWordDTO keyWordDTO) {
        KeyWordModel model = new KeyWordModel();
        model.setId(keyWordDTO.getId());
        model.setKeyWord(keyWordDTO.getKeyWord());
        model.setCount(1);
        return model;
    }

    public Page<KeyWordModel> findAllKeyWordTo(Pageable page) {
        return keyWordRepository.listKeyWord(page).map(keyword -> KeyWordModel.dtoToEntity(keyword));
    }
}
