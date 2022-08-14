package com.example.springboot_cy_marketplace.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "flash_sale")
public class FlashSaleEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "enabled")
    private boolean enabled = true;

    @Column(name = "total_sold")
    private int totalSold = 0;

    @OneToMany(mappedBy = "flashSale", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<FlashSaleItemEntity> items;

    public FlashSaleEntity(String name, String description, LocalDateTime startDate, LocalDateTime endDate, Set<FlashSaleItemEntity> items) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        for(FlashSaleItemEntity item : items){
            item.setCurrentPrice(Double.parseDouble(item.getProductClassified().getNewPrice()));
            item.setFlashSale(this);
        }
        this.items = items;
    }

    public void setStartDate(Date dateToConvert) {
        this.startDate = dateToConvert.toInstant()
                .atZone(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();
    }

    public void setEndDate(Date dateToConvert) {
        this.endDate = dateToConvert.toInstant()
                .atZone(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();
    }
}
