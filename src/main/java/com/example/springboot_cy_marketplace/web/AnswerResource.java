package com.example.springboot_cy_marketplace.web;

import com.example.springboot_cy_marketplace.config.Position;
import com.example.springboot_cy_marketplace.dto.AnswerDTO;
import com.example.springboot_cy_marketplace.dto.ResponseDTO;
import com.example.springboot_cy_marketplace.model.AnswerModel;
import com.example.springboot_cy_marketplace.model.UserModel;
import com.example.springboot_cy_marketplace.services.IAnswerService;
import com.example.springboot_cy_marketplace.services.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api/v1/answer")
public class AnswerResource {
    @Autowired
    IAnswerService iAnswerService;
    @Autowired
    IUserService iUserService;

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 2:38 CH
     * @description-VN:  Lấy ra danh sách câu trả lời của một câu hỏi thông qua id của câu hỏi đó
     * @description-EN:  Get a list of answers to a question by the question's id
     * @param: id,pageable
     * @return:
     *
     * */
    @GetMapping("/get-by-id-question/{id}")
    public Object getAnswerByIdQuestion(@PathVariable Long id, Pageable pageable) {
        return ResponseDTO.of(iAnswerService.getAllByIdQuestion(id, pageable).map(
                ans ->
                        AnswerDTO.builder()
                                .id(ans.getId())
                                .content(ans.getContent())
                                .idQuestion(ans.getIdQuestion())
                                .userDTO(UserModel.toDTO(iUserService.findById(ans.getIdUser()))).build()
        ), "Get all answer by id question");
    }


    /*
     * @author: HaiPhong
     * @since: 10/06/2022 2:41 CH
     * @description-VN:  Lưu câu trả lời cho một câu hỏi
     * @description-EN:  Save the answer to a question
     * @param: answerDTO
     * @return:
     *
     * */
//    @RolesAllowed(Position.BUYER)
    @PostMapping("/add")
    public Object saveAnswer(@RequestBody AnswerDTO answerDTO) {
        return ResponseDTO.of(iAnswerService.add(answerDTO), "Add answer");
    }
}
