package com.example.springboot_cy_marketplace.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Class này tương ứng với bảng city trong cơ sở dữ liệu, chứa dữ liệu về tên các tỉnh thành ở Việt Nam.
 * This class similar to city table in database, storage information about province's names in Viet Nam.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "city")
public class CityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    private String type;
    @Column(name = "slug")
    private String slug;
    @Column(name = "id_ghn")
    private Long idGHN;
}
