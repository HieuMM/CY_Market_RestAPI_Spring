package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.entity.AddressEntity;
import com.example.springboot_cy_marketplace.model.AddressModel;
import com.example.springboot_cy_marketplace.repository.IAddressRepository;
import com.example.springboot_cy_marketplace.services.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired
    IAddressRepository addressRepository;

    @Override
    public List<AddressEntity> findAll() {
        return null;
    }

    @Override
    public Page<AddressEntity> findAll(Pageable page) {
        return null;
    }

    @Override
    public AddressEntity findById(Long id) {
        return null;
    }

    @Override
    public AddressEntity add(AddressModel model) {
        return null;
    }

    @Override
    public List<AddressEntity> add(List<AddressModel> model) {
        return null;
    }

    @Override
    public AddressEntity update(AddressModel model) {
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        if (addressRepository.findById(id) == null) {
            return false;
        } else {
            addressRepository.deleteById(id);
            return true;
        }
    }

    @Override
    public boolean deleteByIds(List<Long> id) {
        return false;
    }


    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:15 CH
     * @description-VN:  Tìm kiếm địa chỉ của người dùng
     * @description-EN:  Search the user's address
     * @param: city, district, ward, address
     * @return: AddressEntity
     *
     * */
    @Override
    public AddressEntity findAddressEntity(String city, String district, String ward, String address) {
        return addressRepository.findByCityAndDistrictAndWardAndAddress(city, district, ward, address);
    }
    /*
     * @author: HieuMM
     * @since: 21-Jun-22 9:04 AM
     * @description-VN:  Hiển thị tất cả địa chỉ người dùng
     * @description-EN:  Show list address of every user
     * @param: id(User)
     * */
    public Page<AddressModel> findAllByUserId(Pageable page, Long id) {
        return addressRepository.findAllByUserEntityList_id(page,id).map(address -> AddressModel.entityToModel(address));
    }
}
