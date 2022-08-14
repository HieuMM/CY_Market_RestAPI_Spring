package com.example.springboot_cy_marketplace.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notify_category")
public class NotifyCategoryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String defaultUrl;

    @OneToMany(mappedBy = "notifyCategoryEntity", fetch = FetchType.LAZY)
    private List<NoticesLocalEntity> noticesLocalEntities;
}
