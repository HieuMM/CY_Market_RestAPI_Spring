package com.example.springboot_cy_marketplace.entity;

import com.example.springboot_cy_marketplace.dto.QuestionDTO;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Class này tương ứng với bảng question_category trong cơ sở dữ liệu, chứa dữ liệu về các danh mục câu hỏi trong phần hỏi đáp.
 * This class similar to question_category table in database, storage information about question's category in ask and answer function.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "question_category")
@NamedNativeQuery(name = "QuestionCategoryEntity.getQuestionsByCategoryId",
                    query = "SELECT q.id AS id, q.title AS title, q.content AS content, q.img1 AS img1, " +
                            "q.img2 AS img2, q.img3 AS img3, q.user_id AS userId, q.status AS status, q.create_date AS createdDate, " +
                            "q.create_by AS createdBy, q.modified_by AS modifiedBy, q.modified_date AS modifiedDate, u.full_name AS userFullName, " +
                            "u.email AS userEmail FROM question q INNER JOIN user u ON q.user_id = u.id INNER JOIN question_category c ON q.category_id = c.id " +
                            "WHERE q.category_id = :id", resultSetMapping = "Mapping.QuestionDTO")
@SqlResultSetMapping(name = "Mapping.QuestionDTO", classes = {@ConstructorResult(targetClass = QuestionDTO.class,
                        columns = {@ColumnResult(name = "id", type = Long.class), @ColumnResult(name = "title", type = String.class),
                                    @ColumnResult(name = "content", type = String.class), @ColumnResult(name = "img1", type = String.class),
                                    @ColumnResult(name = "img2", type = String.class), @ColumnResult(name = "img3", type = String.class),
                                    @ColumnResult(name = "userId", type = Long.class), @ColumnResult(name = "status", type = String.class),
                                    @ColumnResult(name = "createdDate", type = String.class), @ColumnResult(name = "createdBy", type = String.class),
                                    @ColumnResult(name = "modifiedBy", type = String.class), @ColumnResult(name = "modifiedDate", type = String.class),
                                    @ColumnResult(name = "categoryId", type = Long.class), @ColumnResult(name = "userFullName", type = String.class),
                                    @ColumnResult(name = "userEmail", type = String.class), @ColumnResult(name = "avatarUrl", type = String.class),
                                    @ColumnResult(name = "categoryName", type = String.class)})})
public class QuestionCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description")
    private String description;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "questionCategory")
    private List<QuestionEntity> questionEntityList;
}
