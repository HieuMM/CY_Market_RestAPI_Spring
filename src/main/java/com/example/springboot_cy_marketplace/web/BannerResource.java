package com.example.springboot_cy_marketplace.web;

import com.example.springboot_cy_marketplace.config.BannerExcelExporterConfig;
import com.example.springboot_cy_marketplace.dto.BannerDTO;
import com.example.springboot_cy_marketplace.dto.ResponseDTO;
import com.example.springboot_cy_marketplace.entity.BannerEntity;
import com.example.springboot_cy_marketplace.repository.IBannerRepository;
import com.example.springboot_cy_marketplace.services.IBannerService;
import com.example.springboot_cy_marketplace.services.impl.BannerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api/v1/banner")
public class BannerResource {
    @Autowired
    IBannerService iBannerService;
    @Autowired
    BannerServiceImpl bannerService;
    @Autowired
    IBannerRepository bannerRepository;

    @GetMapping
    public Object findAll(Pageable pageable) {
        return ResponseDTO.of(iBannerService.findAll(pageable), "Find all banner");
    }

    @PostMapping
    public Object saveBanner(BannerDTO bannerDTO) {
        return ResponseDTO.of(iBannerService.add(bannerDTO), "Save banner");
    }

    @PutMapping
    public Object updateBanner(BannerDTO bannerDTO) {
        return ResponseDTO.of(iBannerService.update(bannerDTO), "Update banner");
    }

    @DeleteMapping("/{id}")
    public Object deleteBanner(@PathVariable(value = "id") Long id) {
        return ResponseDTO.of(iBannerService.deleteById(id), "Update banner");
    }

    @GetMapping("/find-by-id")
    public Object findByID(@RequestParam(value = "id") int id) {
        return ResponseDTO.of(iBannerService.findBannerById((long) id), "Find banner by id");
    }

    /*
     * @author: HieuMM
     * @since: 29-Jun-22 10:35 AM
     * @description-VN:  Tong so banner
     * @description-EN:
     * @param:
     * */
    @GetMapping("/totalBanner")
    public Object averageRate() {
        return ResponseDTO.of(bannerService.totalBanner(), "total banner");
    }

}
