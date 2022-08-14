package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.entity.DistrictEntity;
import com.example.springboot_cy_marketplace.model.DistrictModel;
import com.example.springboot_cy_marketplace.repository.IDistrictRepository;
import com.example.springboot_cy_marketplace.services.IDistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DistrictServiceImpl implements IDistrictService {
    @Autowired
    IDistrictRepository districtRepository;

    public DistrictEntity toEntity(DistrictModel districtModel) {
        DistrictEntity districtEntity = new DistrictEntity();
        districtEntity.setId(districtModel.getId());
        districtEntity.setName(districtModel.getName());
        districtEntity.setType(districtModel.getType());
        districtEntity.setCity(districtModel.getCity());
        return districtEntity;
    }

    @Override
    public List<DistrictEntity> findAll() {
        return null;
    }

    @Override
    public Page<DistrictEntity> findAll(Pageable page) {
        return null;
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:19 CH
     * @description-VN:  Tìm kiếm quận/huyện
     * @description-EN:  Search district/district
     * @param: id
     * @return: DistrictEntity
     *
     * */
    @Override
    public DistrictEntity findById(Long id) {
        Optional<DistrictEntity> optionalDistrictEntity = districtRepository.findById(id);
        if (optionalDistrictEntity.isPresent()) {
            return optionalDistrictEntity.get();
        } else {
            return null;
        }
    }

    @Override
    public DistrictEntity add(DistrictModel model) {
        return null;
    }

    @Override
    public List<DistrictEntity> add(List<DistrictModel> model) {
        return null;
    }

    @Override
    public DistrictEntity update(DistrictModel model) {
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
}
