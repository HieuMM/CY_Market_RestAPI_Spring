package com.example.springboot_cy_marketplace.web;

import com.example.springboot_cy_marketplace.dto.CityDTO;
import com.example.springboot_cy_marketplace.dto.DistrictDTO;
import com.example.springboot_cy_marketplace.dto.ResponseDTO;
import com.example.springboot_cy_marketplace.dto.WardDTO;
import com.example.springboot_cy_marketplace.repository.ICityRepository;
import com.example.springboot_cy_marketplace.repository.IDistrictRepository;
import com.example.springboot_cy_marketplace.repository.IWardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/address")
public class AddressResource {
    @Autowired
    ICityRepository cityRepository;
    @Autowired
    IDistrictRepository districtRepository;
    @Autowired
    IWardRepository wardRepository;

    /*
     * @author: HieuMM
     * @since: 10-Jun-22 11:24 AM
     * @description-VN:  hiển thị tất cả các tỉnh ở Viêt Nam
     * @description-EN:  load all city in Viet Nam
     * @param:
     * @return:
     *
     * */

    @GetMapping
    @Transactional
    public Object getAllCity() {
        return ResponseDTO.of(this.cityRepository.findAll().stream().map(ans -> CityDTO.toDto(ans)), "Get all");
    }

    /*
     * @author: HaiPhong
     * @since: 10-Jun-22 11:24 AM
     * @description-VN:  hiển thị các quận huyện theo id của thành phố
     * @description-EN:  load all district by id of city
     * @param: id
     * @return:
     *
     * */
    @GetMapping("/loadDistrict/{id}")
    public Object getAllDistrictByCity(@PathVariable Long id) {
        return ResponseDTO.of(this.districtRepository.findByCity(cityRepository.findById(id).get()).stream().map(ans -> DistrictDTO.toDto(ans)), "Get all district by city");
    }

    /*
     * @author: HieuMM
     * @since: 10-Jun-22 11:27 AM
     * @description-VN:  hiển thị các xã phường theo id của quận huyện
     * @description-EN:  load all Ward by id of district
     * @param:   id
     * @return:
     *
     * */
    @GetMapping("/loadWard/{id}")
    public Object getWardByDistrict(@PathVariable Long id) {
        return ResponseDTO.of(this.wardRepository.findByDistrict(districtRepository.findById(id).get()).stream().map(ans -> WardDTO.toDto(ans)), "Get all district by district");
    }


}
