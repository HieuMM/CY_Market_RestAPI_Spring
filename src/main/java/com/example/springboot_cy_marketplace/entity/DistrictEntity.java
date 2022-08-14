package com.example.springboot_cy_marketplace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Class này tương ứng với bảng district trong cơ sở dữ liệu, chứa dữ liệu về tên các quận/huyện ở Việt Nam.
 * This class similar to district table in database, storage information about district's names in Viet Nam.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "district")
public class DistrictEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cityId", nullable = false)
    @JsonIgnore
    private CityEntity city;
    @Column(name = "id_ghn")
    private Long idGHN;
}
