package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.dto.QuestionCategoryDTO;
import com.example.springboot_cy_marketplace.dto.QuestionDTO;
import com.example.springboot_cy_marketplace.model.Constant;
import com.example.springboot_cy_marketplace.model.QuestionCategoryModel;
import com.example.springboot_cy_marketplace.model.QuestionModel;
import com.example.springboot_cy_marketplace.repository.IQuestionCategoryRepository;
import com.example.springboot_cy_marketplace.repository.IQuestionRepository;
import com.example.springboot_cy_marketplace.services.IQuestionCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class QuestionCategoryServiceImpl implements IQuestionCategoryService {

    @Autowired
    IQuestionCategoryRepository questionCategoryRepository;

    @Autowired
    IQuestionRepository questionRepository;

    @Override
    public List<QuestionCategoryModel> findAll() {
        return null;
    }


    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:43 CH
     * @description-VN:  Hàm này trả về danh sách các danh mục câu hỏi trong cơ sở dữ liệu
     * @description-EN:  This function return all question's categories in database include pagination
     * @param: page
     * @return: Page<QuestionCategoryModel>
     *
     * */
    @Override
    public Page<QuestionCategoryModel> findAll(Pageable page) {
        return questionCategoryRepository.findAll(page).map(questionEntity -> QuestionCategoryModel.entityToModel(questionEntity));
    }

    @Override
    public QuestionCategoryModel findById(Long id) {
        return null;
    }

    @Override
    public QuestionCategoryModel add(QuestionCategoryDTO dto) {
        return null;
    }

    @Override
    public List<QuestionCategoryModel> add(List<QuestionCategoryDTO> dto) {
        return null;
    }

    @Override
    public QuestionCategoryModel update(QuestionCategoryDTO dto) {
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public boolean deleteByIds(List<Long> id) {
        return false;
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:43 CH
     * @description-VN:  Hàm này trả về danh sách các câu hỏi theo mã danh mục.
     * @description-EN:  This function return list of questions follow by question's category id
     * @param: categoryId, pageable
     * @return: List<QuestionDTO
     *
     * */
    public Page<QuestionDTO> getAllQuestionByCategoryId(Long categoryId, Pageable pageable, String type) {
        if(type == null){
            type = "all";
        }
        if(type.equals("homepage")) {
//            List<QuestionModel> answered = questionRepository.findAllByQuestionCategory_IdAndStatus(categoryId, Constant.QUESTION_ANSWERED, pageable).stream().map(QuestionModel::entityToModel).collect(Collectors.toList());
//            List<QuestionModel> unanswered = questionRepository.findAllByQuestionCategory_IdAndStatus(categoryId, Constant.QUESTION_SEND, pageable).stream().map(QuestionModel::entityToModel).collect(Collectors.toList());
//            List<QuestionModel> total = Stream.concat(answered.stream(), unanswered.stream()).collect(Collectors.toList());
//            final long start = pageable.getOffset();
//            final long end = Math.min(start + pageable.getPageSize(), total.size());
//            return new PageImpl<>(total.subList((int) start, (int) end).stream().map(QuestionModel::modelToDTO).collect(Collectors.toList()), pageable, total.size());
            return questionRepository.findAllByQuestionCategory_Id(categoryId, pageable).map(QuestionDTO::toDto);
        }
        return questionRepository.findAllByQuestionCategory_Id(categoryId, pageable).map(QuestionDTO::toDto);
    }
}
