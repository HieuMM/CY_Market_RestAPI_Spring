package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.dto.QuestionDTO;
import com.example.springboot_cy_marketplace.entity.QuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IQuestionRepository extends JpaRepository<QuestionEntity,Long> {
    QuestionEntity findByTitleAndContent(String title, String content);

    @Query(value = "SELECT * FROM question q INNER JOIN user u ON q.user_id = u.id " +
            "WHERE q.title LIKE %?1% OR u.full_name LIKE %?1%", nativeQuery = true)
    List<QuestionEntity> findByKeyword(String keyword);

    @Query(value = "SELECT * FROM question q INNER JOIN user u ON q.user_id = u.id " +
            "WHERE q.title LIKE %?1% OR u.full_name LIKE %?1% AND q.status = ?2", nativeQuery = true)
    List<QuestionEntity> findByKeywordAndStatus(String keyword, String status);
    @Query(nativeQuery = true)
    List<QuestionDTO> findAllByUserEntity_Id(Long userId);
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM question WHERE id = ?1", nativeQuery = true)
    void deleteCustomQuery(Long id);

    Page<QuestionEntity> findAllByStatus(String status, Pageable pageable);

    List<QuestionEntity> findAllByStatus(String status);

//    List<QuestionEntity> findAllByStatusAndQuestionCategory_Id(String status, Long id, Pageable pageable);
//
//    List<QuestionEntity> findAllByStatusAndQuestionCategory_IdAndAndTitleContaining(String status, Long id, String title, Pageable pageable);
//
//    List<QuestionEntity> findAllByStatusAndQuestionCategory_IdAndAndTitleContainingAndUserEntity_Id(String status, Long id, String title, Long userId, Pageable pageable);

    @Query("SELECT q FROM QuestionEntity q WHERE q.status != 'Từ chối' AND q.questionCategory.id = ?1")
    Page<QuestionEntity> findAllByQuestionCategory_Id(Long id, Pageable pageable);

    Page<QuestionEntity> findAllByQuestionCategory_IdAndStatus(Long id, String status, Pageable pageable);

    Page<QuestionEntity> findAllByQuestionCategory_IdAndStatusAndTitleContaining(Long id, String status, String title, Pageable pageable);

    Page<QuestionEntity> findAllByUserEntity_IdAndStatus(Long id, String status, Pageable pageable);

    Page<QuestionEntity> findAllByUserEntity_IdAndStatusAndTitleContaining(Long id, String status, String title, Pageable pageable);



}
