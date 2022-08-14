package com.example.springboot_cy_marketplace.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Class này tương ứng với bảng user trong cơ sở dữ liệu, lưu trữ thông tin về tài khoản truy cập hệ thống.
 * This class similar to user table in database, storage information about account to access to our system.
 */
@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "full_name")
    private String fullName;
    private String password;
    private boolean enabled = false;
    @Column(name = "avatar_url")
    private String avatar;
    @Column(name = "reset_password_token")
    private String resetPassWordToken;
    @Column(name = "account_non_locked")
    private boolean accountNonLocked = true;
    @Column(name = "failed_attempt")
    private int failedAttempt;
    @Column(name = "lock_time")
    private Date lockTime;
    private boolean rememberme;
    @Column(name = "verify_code")
    private int verifyCode;
    @Column(name = "phone_number")
    private String phoneNumber="0";
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_role"))
    private List<RoleEntity> roleEntityList;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "user_address", joinColumns = @JoinColumn(name = "user_id")
                ,inverseJoinColumns = @JoinColumn(name = "address_id"))
    private List<AddressEntity> addressEntityList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntity")
    private List<QuestionEntity> questionEntityList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntity")
    private List<NoticesLocalEntity> noticesLocalEntityList;

    @OneToMany(mappedBy="userEntity")
    private List<ReviewEntity> reviewList;

    @OneToOne(mappedBy = "userEntity")
    private CartEntity cartEntity;

    @OneToMany(mappedBy = "userEntity")
    private List<OrderEntity> orderList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntitySend")
    private List<UserRoomChatEntity> userRoomChatEntitiesSend;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntityReceive")
    private List<UserRoomChatEntity> userRoomChatEntitiesReceive;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntity")
    private List<VoucherUserEntity> voucherUserList;

}
