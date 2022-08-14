package com.example.springboot_cy_marketplace.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "voucher")
public class VoucherEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "categoryVoucher")
    private String categoryVoucher;
    @Column(name = "nameProgram")
    private String nameProgram;
    @Column(name = "codeVoucher")
    private String codeVoucher;
    @Column(name = "startDate")
    private Date startDate;
    @Column(name = "endDate")
    private Date endDate;
    @Column(name = "discountPercent")
    private int discountPercent;
    @Column(name = "minValueOrder")
    private int minValueOrder;//gia tri cao nhat de su dung voucher
    @Column(name = "maxValueCanReduce")
    private int maxValueCanReduce;//gia tri lon nhat co the giam
    @Column(name = "amountVoucher")
    private int amountVoucher;
    @Column(name="amountUsed")
    private int amountUsed;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "voucherEntity")
    private List<VoucherUserEntity> voucherUserList;
}
