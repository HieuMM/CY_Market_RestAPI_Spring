package com.example.springboot_cy_marketplace.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Class này tương ứng với bảng notices_local trong cơ sở dữ liệu, chứa dữ liệu về các thông báo chung cho người dùng.
 * This class similar to notices_local table in database, storage information about public announcement for users.
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Data
@Table(name = "notices_local")
public class NoticesLocalEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;
    @Column(name = "img1")
    private String img1;
    @Column(name = "img2")
    private String img2;
    @Column(name = "img3")
    private String img3;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private NotifyCategoryEntity notifyCategoryEntity;
}
