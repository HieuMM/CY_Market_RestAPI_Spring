package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.dto.BannerDTO;
import com.example.springboot_cy_marketplace.entity.BannerEntity;
import com.example.springboot_cy_marketplace.model.BannerModel;
import com.example.springboot_cy_marketplace.repository.IBannerRepository;
import com.example.springboot_cy_marketplace.services.AmazonClient;
import com.example.springboot_cy_marketplace.services.IBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerServiceImpl implements IBannerService {
    @Autowired
    IBannerRepository bannerRepository;
    @Autowired
    AmazonClient amazonClient;

    @Override
    public List<BannerModel> findAll() {
        return null;
    }

    @Override
    public Page<BannerModel> findAll(Pageable page) {
        return bannerRepository.findAll(page).map(ban-> BannerModel.entityToModel(ban));
    }

    @Override
    public BannerModel findById(Long id) {
        return BannerModel.entityToModel(bannerRepository.findById(id).get());
    }

    @Override
    public BannerDTO findBannerById(Long id) {
        return BannerDTO.entityToDTO(bannerRepository.findById(id).get());
    }

    @Override
    public BannerModel add(BannerDTO dto) {
        BannerEntity bannerEntity = BannerDTO.dtoToEntity(dto);
        if (dto.getImage() != null && !dto.getImage().isEmpty() ) {
            bannerEntity.setImage(amazonClient.uploadFilewithFolder(dto.getImage(), "banner"));
        }else {
            bannerEntity.setImage(dto.getImageString());
        }

        return BannerModel.entityToModel(bannerRepository.save(bannerEntity));
    }

    @Override
    public List<BannerModel> add(List<BannerDTO> dto) {
        return null;
    }

    @Override
    public BannerModel update(BannerDTO dto) {
        BannerEntity bannerEntity = BannerDTO.dtoToEntity(dto);
        if (dto.getImage() != null) {
            bannerEntity.setImage(amazonClient.uploadFilewithFolder(dto.getImage(), "banner"));
        }else {
            bannerEntity.setImage(dto.getImageString());
        }

        return BannerModel.entityToModel(bannerRepository.save(bannerEntity));
    }

    @Override
    public boolean deleteById(Long id) {
        bannerRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean deleteByIds(List<Long> id) {
        return false;
    }
    /*
    * @author: HieuMM
    * @since: 29-Jun-22 10:31 AM
    * @description-VN:  Total banner
    * @description-EN:
    * @param:
    * */
    public Object totalBanner() {
        int sum= bannerRepository.totalBanner();
        return sum;
    }
}
