package com.example.springboot_cy_marketplace.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.springboot_cy_marketplace.model.Constant;
import lombok.*;

import javax.persistence.*;

/**
 * Class này tương ứng với bảng question trong cơ sở dữ liệu, chứa dữ liệu về các câu hỏi trong phần hỏi đáp.
 * This class similar to question table in database, storage information about questions in ask and answer function.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "question")
@Entity
@NamedNativeQuery(name = "QuestionEntity.findAllByUserEntity_Id",
                    query = "SELECT q.id AS id, q.title AS title, q.content AS content, " +
                            "q.img1 AS img1, q.img2 AS img2, q.img3 AS img3, q.user_id AS userId, q.status AS status, q.create_date AS createdDate, " +
                            "q.create_by AS createdBy, q.modified_by AS modifiedBy, q.modified_date AS modifiedDate, q.category_id AS categoryId, u.full_name AS userFullName, " +
                            "u.email AS userEmail, u.avatar_url AS avatarUrl, c.name AS categoryName FROM question q INNER JOIN user u ON q.user_id = u.id " +
                            "INNER JOIN question_category c ON q.category_id = c.id WHERE q.user_id = :userId", resultSetMapping = "Mapping.QuestionDTO")

public class QuestionEntity extends BaseEntity{
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
    @Column(name = "status")
    private String status = Constant.QUESTION_SEND;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private QuestionCategoryEntity questionCategory;
}
