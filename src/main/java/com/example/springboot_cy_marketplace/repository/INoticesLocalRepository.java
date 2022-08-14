package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.NoticesLocalEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface INoticesLocalRepository extends JpaRepository<NoticesLocalEntity, Long> {
    @Query("SELECT u FROM NoticesLocalEntity u where u.userEntity.id =:id ORDER BY u.modifiedDate DESC ")
    List<NoticesLocalEntity> findAllByUserEntity_IdAndDescTime(@Param("id") Long id);

    List<NoticesLocalEntity> findAllByUserEntity_Id(Long id);

    @Query("SELECT u FROM NoticesLocalEntity u where u.userEntity.id =:id OR u.userEntity.id is null ")
    Page<NoticesLocalEntity> findAllOwnerNotices(@Param("id") Long id, Pageable pageable);

    @Query("SELECT u FROM NoticesLocalEntity u where u.userEntity.id is null")
    Page<NoticesLocalEntity> findAllNoticesLocal(Pageable pageable);
}
