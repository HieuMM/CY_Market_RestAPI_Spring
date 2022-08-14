package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findByEmail(String email);
    UserEntity findByEmailAndVerifyCode(String email, int verifyCode);
    UserEntity findByResetPassWordToken(String token);
    @Query(value = "UPDATE user SET failed_attempt = :failAttempts WHERE email = :username",nativeQuery = true)
    @Transactional
    @Modifying
    void updateFailedAttempts(@Param("failAttempts") int failAttempts,
                              @Param("username") String username);

    @Query(value = "SELECT * FROM user u WHERE u.email LIKE %:keyword% OR u.full_name LIKE %:keyword%", nativeQuery = true)
    List<UserEntity> findByKeyword(String keyword);

    UserEntity findUserEntityByEmailAndPassword(String email, String password);

    //tong so nguoi dung
    @Query("SELECT count(u.id) FROM UserEntity u ")
    int totalUser();
    //tong so tai khoan bi khoa
    @Query("SELECT count(u.id) FROM UserEntity u where u.enabled = false")
    int totalUserIsLocked();
    //danh sach tai khoan bi khoa
    @Query("SELECT u FROM UserEntity u where u.enabled = :status")
    Page<UserEntity> listUser(Pageable page, @Param("status") Boolean status);
    //new User in 1 week
    @Query("SELECT count(u.id) FROM UserEntity u where u.createDate between :startDate and :endDate")
    int countNewUser(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    //new User in time
    @Query("SELECT u FROM UserEntity u where u.createDate between :startDate and :endDate")
    Page<UserEntity> listUserInTime(@Param("startDate") Date startDate, @Param("endDate") Date endDate,Pageable pageable);


    @Query(value = "SELECT COUNT(*) FROM user u WHERE u.create_date LIKE CONCAT('%', ?1, '%')", nativeQuery = true)
    Long countNewUsersOfDay(String day);
}
