package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.KeyWordEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IKeyWordRepository extends JpaRepository<KeyWordEntity, Long> {
    /*
     * @author: HieuMM
     * @since: 10-Jun-22 11:37 AM
     * @description:hiển thị tất cả các từ khóa của sản phẩm
     * @update:
     * */
    @Query("SELECT u.keyWord FROM KeyWordEntity u")
    List<String> findAllKeyWord();
    KeyWordEntity findByKeyWord(String keyWord);
/*
* @author: HieuMM
* @since: 28-Jun-22 2:38 PM
* @description-VN:  Hiển thị danh sách keyword sắp xếp theo lượng tìm kiếm
* @description-EN:
* @param:
* */
    @Query("SELECT u FROM KeyWordEntity u order by u.count desc ")
    Page<KeyWordEntity> listKeyWord(Pageable pageable);
}
