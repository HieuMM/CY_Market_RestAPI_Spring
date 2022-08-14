package com.example.springboot_cy_marketplace.entity;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "voucher_user")
public class VoucherUserEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name="voucher_id", nullable=false)
    private VoucherEntity voucherEntity;

    @Column(name = "status")
    private Boolean status;
}
