package com.example.springboot_cy_marketplace.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Class này tương ứng với bảng address trong cơ sở dữ liệu, lưu trữ thông tin về địa chỉ của người dùng.
 * This class similar to address table in database, storage information about user's addresses.
 */
@Entity
@Table(name = "address", uniqueConstraints = {@UniqueConstraint(name = "Unique_Address",
        columnNames = {"city", "district", "ward", "address"})})
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "city")
    private String city;
    @Column(name = "district")
    private String district;
    @Column(name = "ward")
    private String ward;
    @Column(name = "address")
    private String address;
    @Column(name = "id_city_ghn")
    private Long idCityGHN;
    @Column(name = "id_district_ghn")
    private Long idDistrictGHN;
    @Column(name = "defaultAdd")
    private Boolean defaultAdd;
    @ManyToMany(mappedBy = "addressEntityList",cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    private List<UserEntity> userEntityList;
}
