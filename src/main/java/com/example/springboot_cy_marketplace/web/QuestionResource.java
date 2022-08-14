package com.example.springboot_cy_marketplace.web;

import com.example.springboot_cy_marketplace.config.Position;
import com.example.springboot_cy_marketplace.dto.QuestionDTO;
import com.example.springboot_cy_marketplace.dto.ResponseDTO;
import com.example.springboot_cy_marketplace.dto.SendQuestionDTO;
import com.example.springboot_cy_marketplace.dto.UpdateQuestionDTO;
import com.example.springboot_cy_marketplace.entity.QuestionEntity;
import com.example.springboot_cy_marketplace.model.QuestionModel;
import com.example.springboot_cy_marketplace.services.impl.QuestionCategoryServiceImpl;
import com.example.springboot_cy_marketplace.services.impl.QuestionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/question")
public class QuestionResource {
    @Autowired
    QuestionServiceImpl questionService;
    @Autowired
    QuestionCategoryServiceImpl questionCategoryService;

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 9:43 SA
    * @description-VN: Tìm câu hỏi theo mã.
    * @description-EN: Find question by id.
    * @param: id - Mã câu hỏi.
    * @return:
    *
    * */
    @GetMapping(value = "/find-by-id")
    public Object getQuestionById(@RequestParam("id")Long id){
        return ResponseDTO.of(questionService.findById(id),"Find by id");
    }

    /*
     * @author: Manh Tran
     * @since: 13/06/2022 9:39 SA
     * @description-VN: Xoá câu hỏi.
     * @description-EN: Delete question.
     * @param: id - Mã câu hỏi.
     * @return:
     *
     * */
    @RolesAllowed(Position.BUYER)
    @DeleteMapping(value = "/delete")
    @Transactional
    public Object deleteQuestion(@RequestParam(value = "id") Long id) {
        boolean result = this.questionService.deleteById(id);
        if(result){
            return ResponseDTO.of(result, "Delete question by id " + id);
        }else {
            return ResponseDTO.of(null, "Delete question by id");
        }
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 9:39 SA
    * @description-VN: Thêm câu hỏi mới.
    * @description-EN: Add new question.
    * @param: sendQuestionDTO - Thông tin câu hỏi.
    * @return:
    *
    * */
    @RolesAllowed(Position.BUYER)
    @PostMapping (value = "/add")
    public Object addQuestion(SendQuestionDTO sendQuestionDTO){
        QuestionModel model = questionService.sendQuestionRequestToModel(sendQuestionDTO);
        QuestionEntity questionEntity = questionService.add(model);
        if(questionEntity == null){
            return ResponseDTO.of(null, "Add question");
        }else {
            return ResponseDTO.of(QuestionDTO.toDto(questionEntity), "Add question");
        }
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 9:38 SA
    * @description-VN: Cập nhật câu hỏi.
    * @description-EN: Update question.
    * @param: updateQuestionDTO - Thông tin cập nhật câu hỏi.
    * @return:
    *
    * */
    @RolesAllowed(Position.BUYER)
    @PostMapping(value = "/update")
    public Object updateQuestion(UpdateQuestionDTO updateQuestionDTO){
        QuestionModel model = questionService.updateQuestionRequestToModel(updateQuestionDTO);
        if(model == null){
            return ResponseDTO.of(null, "Update question");
        }
        QuestionEntity questionEntity = questionService.update(model);
        if(questionEntity == null){
            return ResponseDTO.of(null, "Update question");
        }else {
            return ResponseDTO.of(QuestionDTO.toDto(questionEntity), "Update question");
        }
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 9:37 SA
    * @description-VN: Chuyển trạng thái câu hỏi sang "Từ chối".
    * @description-EN: Change status of question to "Reject".
    * @param: id - Mã câu hỏi.
    * @return:
    *
    * */
    @RolesAllowed(Position.ADMIN)
    @GetMapping(value = "/reject")
    public Object adminRejectQuestion(@RequestParam(value = "id")Long id){
        String message = questionService.rejectQuestion(id);
        return ResponseDTO.show(200, "Reject question", message);
    }

    /*
     * @author: Manh Tran
     * @since: 13/06/2022 9:37 SA
     * @description-VN: Tìm danh sách câu hỏi theo mã danh mục.
     * @description-EN: Find list question by category id.
     * @param: id - Mã danh mục.
     * @return:
     *
     * */
    @GetMapping(value = "/filter/category-id")
    public Object getQuestionsByCategoryId(@RequestParam(value = "id")Long id, Pageable pageable, @RequestParam(value="type")String type){
        return ResponseDTO.of(questionCategoryService.getAllQuestionByCategoryId(id, pageable, type), "Get all questions by category id");
    }

    /*
     * @author: Manh Tran
     * @since: 13/06/2022 9:36 SA
     * @description-VN: Tìm câu hỏi theo mã người dùng.
     * @description-EN: Find question by user id.
     * @param: id - Mã người dùng.
     * @return:
     *
     * */
    @GetMapping(value = "/filter/userId")
    public Object getQuestionsByUserId(@RequestParam(value = "id")Long userId, Pageable pageable){
        Page<QuestionDTO> result = questionService.getAllQuestionByUserId(userId, pageable);
        return ResponseDTO.of(result.getTotalElements() > 0 ? result : null, "Get all questions by user id");
    }

    /*
     * @author: Manh Tran
     * @since: 13/06/2022 9:35 SA
     * @description-VN: Tìm câu hỏi theo từ khoá ở tiêu đề.
     * @description-EN: Find questions by keyword in title.
     * @param: keyword - Từ khoá tìm kiếm.
     * @return:
     *
     * */
    @GetMapping(value = "/search/key-word")
    public Object searchQuestionByKeyword(@RequestParam(value = "keyword") String keyword, Pageable pageable, @RequestParam(value="type")String type){
        return ResponseDTO.of(questionService.findQuestionsByKeyword(keyword, pageable, type), "Find questions by keyword");
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 9:33 SA
    * @description-VN: Lấy danh sách tất cả câu hỏi.
    * @description-EN: Get list all question.
    * @param:
    * @return:
    *
    * */
    @GetMapping(value = "/find-all")
    public Object findAllQuestions(Pageable pageable){
        return ResponseDTO.of(this.questionService.findAll(pageable), "Get all questions");
    }

    /*
     * @author: Manh Tran
     * @since: 13/06/2022 9:30 SA
     * @description-VN: Lấy danh sách các danh mục.
     * @description-EN: Get list of categories.
     * @param:
     * @return:
     *
     * */
    @GetMapping(value = "/find-all-category")
    public Object findAllResources(Pageable pageable){
        return ResponseDTO.of(this.questionCategoryService.findAll(pageable), "Get all question category");
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 8:39 SA
    * @description-VN: Lấy danh sách các câu hỏi chờ trả lời và đã trả lời.
    * @description-EN: Get list questions have status pending and answered.
    * @param:
    * @return:
    *
    * */
    @GetMapping(value = "/find-all-pending-and-answered")
    public Object findAllPendingAndAnswered(Pageable pageable){
        return ResponseDTO.show(200, "Get all pending and answer questions", this.questionService.findAllNormalQuestion(pageable));
    }

    /*
    * @author: Manh Tran
    * @since: 15/06/2022 5:13 CH
    * @description-VN: Lọc câu hỏi theo trạng thái.
    * @description-EN: Filter question by status.
    * @param: status - Trạng thái câu hỏi.
    * @return:
    *
    * */
    @GetMapping(value = "/filter/status")
    public Page<QuestionDTO> filterQuestionByStatus(@RequestParam(value = "status") String status, Pageable pageable){
        return questionService.filterQuestionByStatus(status, pageable);
    }

    /*
    * @author: Manh Tran
    * @since: 15/06/2022 8:28 SA
    * @description-VN: Lọc câu hỏi theo nhiều điều kiện.
    * @description-EN: Filter questions by many conditions.
    * @param: status - Trạng thái câu hỏi.
    * @return:
    *
    * */
    @GetMapping(value = "/filter/conditions")
    public Object filterQuestionByStatus(@RequestParam(value = "type", defaultValue = "0")Integer type, @RequestParam(value = "status", defaultValue = "0") String status,
                                            @RequestParam(value = "categoryId", defaultValue = "0") Long categoryId, @RequestParam(value = "userId", defaultValue = "0") Long userId,
                                                @RequestParam(value = "keyword", defaultValue = "0") String keyword, Pageable pageable){
        return ResponseDTO.show(200, "Filter questions by many conditions",
                this.questionService.findAllByManyConditions(type, status, categoryId, userId, keyword, pageable));
    }
}
