package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.entity.ProductEntity;
import com.example.springboot_cy_marketplace.entity.ProductStatisticalEntity;
import com.example.springboot_cy_marketplace.repository.IProductRepository;
import com.example.springboot_cy_marketplace.repository.IProductStatisticalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ProductStatisticalService {
    @Autowired
    private IProductStatisticalRepository productStatisticalRepository;

    @Autowired
    private IProductRepository productRepository;

    public void add(ProductEntity productEntity, int type) {
        ProductStatisticalEntity productStatistical = new ProductStatisticalEntity();
        productStatistical.setDay(LocalDate.now().getDayOfMonth());
        productStatistical.setMonth(LocalDate.now().getMonthValue());
        productStatistical.setYear(LocalDate.now().getYear());
        productStatistical.setTotalView(type == 1 ? 1 : 0);
        productStatistical.setTotalBuy(type == 2 ? 1 : 0);
        productStatistical.setProductEntity(productEntity);
        productStatisticalRepository.save(productStatistical);
    }

    public void updateView(Long productId) {
        Optional<ProductStatisticalEntity> reportByDate = this.productStatisticalRepository.findByProductEntity_IdAndDayAndMonthAndYear(productId, LocalDate.now().getDayOfMonth(), LocalDate.now().getMonthValue(), LocalDate.now().getYear());
        if (reportByDate.isEmpty()) {
            this.add(productRepository.findById(productId).get(), 1);
        } else {
            reportByDate.get().setTotalView(reportByDate.get().getTotalView() + 1);
            this.productStatisticalRepository.save(reportByDate.get());
        }
    }

    public void updateBuy(Long productId) {
        Optional<ProductStatisticalEntity> reportByDate = this.productStatisticalRepository.findByProductEntity_IdAndDayAndMonthAndYear(productId, LocalDate.now().getDayOfMonth(), LocalDate.now().getMonthValue(), LocalDate.now().getYear());
        if (reportByDate.isEmpty()) {
            this.add(productRepository.findById(productId).get(), 2);
        } else {
            reportByDate.get().setTotalBuy(reportByDate.get().getTotalBuy() + 1);
            this.productStatisticalRepository.save(reportByDate.get());
        }
    }
}
