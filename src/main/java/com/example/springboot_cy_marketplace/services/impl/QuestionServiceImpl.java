package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.dto.QuestionDTO;
import com.example.springboot_cy_marketplace.dto.SendQuestionDTO;
import com.example.springboot_cy_marketplace.dto.UpdateQuestionDTO;
import com.example.springboot_cy_marketplace.entity.QuestionCategoryEntity;
import com.example.springboot_cy_marketplace.entity.QuestionEntity;
import com.example.springboot_cy_marketplace.model.Constant;
import com.example.springboot_cy_marketplace.model.QuestionModel;
import com.example.springboot_cy_marketplace.repository.IQuestionCategoryRepository;
import com.example.springboot_cy_marketplace.repository.IQuestionRepository;
import com.example.springboot_cy_marketplace.repository.IUserRepository;
import com.example.springboot_cy_marketplace.services.AmazonClient;
import com.example.springboot_cy_marketplace.services.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class QuestionServiceImpl implements IQuestionService {
    @Autowired
    IQuestionRepository questionRepository;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IQuestionCategoryRepository questionCategoryRepository;

    @Autowired
    AmazonClient amazonClient;


    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:44 CH
     * @description-VN:  Hàm này chuyển entity sang model để trả về cho người dùng
     * @description-EN:  This function convert from entity to model and return to client.
     * @param: questionModel
     * @return: QuestionEntity
     *
     * */
    public QuestionEntity toEntity(QuestionModel questionModel) {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setId(questionModel.getId());
        questionEntity.setTitle(questionModel.getTitle());
        questionEntity.setContent(questionModel.getContent());
        questionEntity.setImg1(questionModel.getImg1());
        questionEntity.setImg2(questionModel.getImg2());
        questionEntity.setImg3(questionModel.getImg3());
        Optional<QuestionCategoryEntity> category = questionCategoryRepository.findById(questionModel.getCategoryId());
        if (category.isPresent()) {
            questionEntity.setQuestionCategory(category.get());
        } else {
            questionEntity.setQuestionCategory(new QuestionCategoryEntity());
        }
//        questionEntity.setStatus(questionModel.getStatus());
        try {
            questionEntity.setUserEntity(userRepository.findById(questionModel.getUserId()).get());
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return questionEntity;
    }

    @Override
    public List<QuestionModel> findAll() {
        return null;
    }


    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:45 CH
     * @description-VN:  Hàm này trả về danh sách các câu hỏi, kết hợp phân trang.
     * @description-EN:  This function return list of questions include pagination.
     * @param: page
     * @return: Page<QuestionModel>
     *
     * */
    @Override
    public Page<QuestionModel> findAll(Pageable page) {
        Pageable page2 = PageRequest.of(page.getPageNumber(), page.getPageSize(), Sort.by("createDate").descending());
        return questionRepository.findAll(page2).map(QuestionModel::entityToModel);
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:45 CH
     * @description-VN:  Hàm này trả về câu hỏi theo mã của câu hỏi ấy.
     * @description-EN:  This function return question follow by it's id.
     * @param: id
     * @return: QuestionModel
     *
     * */
    @Override
    public QuestionModel findById(Long id) {
        Optional<QuestionEntity> result = questionRepository.findById(id);
        return result.map(QuestionModel::entityToModel).orElse(null);
    }

    @Override
    public QuestionModel add(QuestionDTO model) {
        return null;
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:45 CH
     * @description-VN:  Hàm này để thêm câu hỏi
     * @description-EN:  This function for adding question
     * @param: questionModel
     * @return: QuestionEntity
     *
     * */
    @Override
    public QuestionEntity add(QuestionModel questionModel) {
        if (this.findByTitleAndContent(questionModel.getTitle(), questionModel.getContent()) == null) {
            QuestionEntity questionEntity = toEntity(questionModel);
            if (questionEntity.getUserEntity() == null) {
                return null;
            } else {
                return questionRepository.save(questionEntity);
            }
        } else {
            return null;
        }
    }


    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:46 CH
     * @description-VN:  Lấy danh sách câu hỏi theo trạng thái
     * @description-EN:  Get a list of questions by status
     * @param: status, pageable
     * @return: Page<QuestionModel>
     *
     * */
    @Override
    public Page<QuestionModel> findQuestionNotYetAnswered(String status, Pageable pageable) {
        return questionRepository.findAllByStatus(status, pageable).map(QuestionModel::entityToModel);
    }

    @Override
    public List<QuestionModel> add(List<QuestionDTO> model) {
        return null;
    }

    @Override
    public QuestionModel update(QuestionDTO model) {
        return null;
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:47 CH
     * @description-VN:  Hàm này để cập nhật câu hỏi
     * @description-EN:  This function for updating question.
     * @param: model
     * @return: QuestionEntity
     *
     * */
    @Override
    public QuestionEntity update(QuestionModel model) {
        Optional<QuestionEntity> fQuestion = questionRepository.findById(model.getId());

        fQuestion.get().setId(model.getId());
        fQuestion.get().setTitle(model.getTitle());
        fQuestion.get().setContent(model.getContent());
        fQuestion.get().setImg1(model.getImg1());
        fQuestion.get().setImg2(model.getImg2());
        fQuestion.get().setImg3(model.getImg3());
        fQuestion.get().setUserEntity(userRepository.findById(model.getUserId()).get());
        fQuestion.get().setQuestionCategory(questionCategoryRepository.findById(model.getCategoryId()).get());
        return questionRepository.save(fQuestion.get());
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:47 CH
     * @description-VN:  Hàm này để xoá câu hỏi theo mã câu hỏi.
     * @description-EN:  This function for deleting question follow by it's id.
     * @param:
     * @return:
     *
     * */
    @Override
    public boolean deleteById(Long id) {
        try {
            questionRepository.deleteCustomQuery(id);
            Optional<QuestionEntity> result = questionRepository.findById(id);
            return result.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteByIds(List<Long> id) {
        return false;
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:48 CH
     * @description-VN:  Hàm này tìm câu hỏi dựa vào tiêu đề và nội dung để tránh trùng lặp câu hỏi.
     * @description-EN:  This function for finding question follow by title and content to bypass duplicate question.
     * @param: title, content
     * @return: QuestionEntity
     *
     * */
    public QuestionEntity findByTitleAndContent(String title, String content) {
        return questionRepository.findByTitleAndContent(title, content);
    }


    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:48 CH
     * @description-VN:  Hàm này để chuyển dữ liệu thêm câu hỏi nhận vào từ API thành lớp model.
     * @description-EN:  This function for converting data received from API to model class.
     * @param: sendQuestionDTO
     * @return: QuestionModel
     *
     * */
    public QuestionModel sendQuestionRequestToModel(SendQuestionDTO sendQuestionDTO) {
        QuestionModel model = new QuestionModel();
        model.setTitle(sendQuestionDTO.getTitle());
        model.setContent(sendQuestionDTO.getContent());
        try {
            if (sendQuestionDTO.getImg()[0] != null) {
                model.setImg1(amazonClient.uploadFilewithFolder(sendQuestionDTO.getImg()[0], "question"));
            }
            if (sendQuestionDTO.getImg()[1] != null) {
                model.setImg2(amazonClient.uploadFilewithFolder(sendQuestionDTO.getImg()[1], "question"));
            }
            if (sendQuestionDTO.getImg()[2] != null) {
                model.setImg3(amazonClient.uploadFilewithFolder(sendQuestionDTO.getImg()[2], "question"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.setUserId(sendQuestionDTO.getUserId());
        model.setCategoryId(sendQuestionDTO.getCategoryId());
//        model.setStatus(Constant.QUESTION_SEND);
        return model;
    }


    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:49 CH
     * @description-VN:  Hàm này để chuyển dữ liệu cập nhật câu hỏi nhận vào từ API thành lớp model.
     * @description-EN:  This function for converting data received from API to model class.
     * @param: updateQuestionRequest
     * @return: QuestionModel
     *
     * */
    public QuestionModel updateQuestionRequestToModel(UpdateQuestionDTO updateQuestionRequest) {
        Optional<QuestionEntity> optionalQuestionEntity = this.questionRepository.findById(updateQuestionRequest.getId());
        if (optionalQuestionEntity.isEmpty()) {
            return null;
        }
        QuestionModel model = new QuestionModel();
        model.setId(updateQuestionRequest.getId());
        model.setTitle(updateQuestionRequest.getTitle());
        model.setContent(updateQuestionRequest.getContent());

        if (updateQuestionRequest.getOldImageIds() != null && updateQuestionRequest.getOldImageIds().size() > 0) {
            if (updateQuestionRequest.getOldImageIds().contains(1L)) {
                model.setImg1(optionalQuestionEntity.get().getImg1());
            }
            if (updateQuestionRequest.getOldImageIds().contains(2L)) {
                model.setImg2(optionalQuestionEntity.get().getImg2());
            }
            if (updateQuestionRequest.getOldImageIds().contains(3L)) {
                model.setImg3(optionalQuestionEntity.get().getImg3());
            }
        }
        if (updateQuestionRequest.getImg() != null) {
            for (MultipartFile file : updateQuestionRequest.getImg()) {
                if (model.getImg1() == null) {
//                    if (optionalQuestionEntity.get().getImg1() != null) {
//                            amazonClient.deleteFileFromS3Bucket(optionalQuestionEntity.get().getImg1());
//                    }
                    model.setImg1(amazonClient.uploadFilewithFolder(file, "question"));
                } else if (model.getImg2() == null) {
//                    if (optionalQuestionEntity.get().getImg2() != null) {
//                           amazonClient.deleteFileFromS3Bucket(optionalQuestionEntity.get().getImg2());
//                    }
                    model.setImg2(amazonClient.uploadFilewithFolder(file, "question"));
                } else if (model.getImg3() == null) {
//                    if (optionalQuestionEntity.get().getImg3() != null) {
//                           amazonClient.deleteFileFromS3Bucket(optionalQuestionEntity.get().getImg3());
//                    }
                    model.setImg3(amazonClient.uploadFilewithFolder(file, "question"));
                }
            }
        }
        model.setUserId(updateQuestionRequest.getUserId());
        model.setCategoryId(updateQuestionRequest.getCategoryId());
        return model;
    }


    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:49 CH
     * @description-VN:  Hàm này để tìm câu hỏi dựa vào từ khoá trùng khớp với tiêu đề của câu hỏi đó.
     * @description-EN:  This function for searching questions have title similar to keyword.
     * @param: keyword, pageable
     * @return: List<QuestionDTO>
     *
     * */
    public Page<QuestionDTO> findQuestionsByKeyword(String keyword, Pageable pageable, String type) {
        if(type == null){
            type = "all";
        }
        if(type.equals("homepage")) {
            List<QuestionModel> answered = questionRepository.findByKeywordAndStatus(keyword, Constant.QUESTION_ANSWERED).stream().map(QuestionModel::entityToModel).collect(Collectors.toList());
            List<QuestionModel> unanswered = questionRepository.findByKeywordAndStatus(keyword, Constant.QUESTION_SEND).stream().map(QuestionModel::entityToModel).collect(Collectors.toList());
            List<QuestionModel> total = Stream.concat(answered.stream(), unanswered.stream()).collect(Collectors.toList());
            final long start = pageable.getOffset();
            final long end = Math.min(start + pageable.getPageSize(), total.size());
            return new PageImpl<>(total.subList((int) start, (int) end).stream().map(QuestionModel::modelToDTO).collect(Collectors.toList()));
        }
        List<QuestionDTO> result = this.questionRepository.findByKeyword(keyword).stream().map(QuestionDTO::toDto).collect(Collectors.toList());
        final long start = pageable.getOffset();
        final long end = Math.min(start + pageable.getPageSize(), result.size());
        return new PageImpl<>(result.subList((int) start, (int) end), pageable, result.size());
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:50 CH
     * @description-VN:  Hàm này để trả về danh sách các câu hỏi theo mã người dùng.
     * @description-EN:  This function return list of questions by id of user created it.
     * @param: userId
     * @return: List<QuestionDTO>
     *
     * */
    public Page<QuestionDTO> getAllQuestionByUserId(Long userId, Pageable pageable) {
        List<QuestionDTO> result = questionRepository.findAllByUserEntity_Id(userId);
        final long start = pageable.getOffset();
        final long end = Math.min(start + pageable.getPageSize(), result.size());
        return new PageImpl<>(result.subList((int) start, (int) end), pageable, result.size());
    }

    /**
     * Hàm này để quản trị viên từ chối những câu hỏi không phù hợp thuần phong mỹ tục.
     * This function for admin permit answer which is not suitable.
     * @param questionId mã câu hỏi muốn từ chối trả lời
     */
    public String rejectQuestion(Long questionId){
        Optional<QuestionEntity> question = this.questionRepository.findById(questionId);
        if(question.isEmpty()){
            return "Can not file question with id " + questionId;
        }
        QuestionEntity questionEntity = question.get();
        questionEntity.setStatus(Constant.QUESTION_DELETE);
        this.questionRepository.save(questionEntity);
        return "Reject question successfully!";
    }

    /*
    * @author: Manh Tran
    * @since: 13/06/2022 8:15 SA
    * @description-VN: Tìm các câu hỏi có trạng thái chờ trả lời và đã trả lời.
    * @description-EN: Find all questions have status pending and answered.
    * @param:
    * @return:
    *
    * */
    public Page<QuestionDTO> findAllNormalQuestion(Pageable pageable){
        Pageable pageable2 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createDate").descending());
        List<QuestionEntity> sendQuestions = new ArrayList<>(questionRepository.findAllByStatus(Constant.QUESTION_SEND));
        List<QuestionEntity> answeredQuestions = new ArrayList<>(questionRepository.findAllByStatus(Constant.QUESTION_ANSWERED));
        List<QuestionEntity> total = Stream.concat(sendQuestions.stream(), answeredQuestions.stream()).collect(Collectors.toList());
        final long start = pageable.getOffset();
        final long end = Math.min(start + pageable.getPageSize(), total.size());
        return new PageImpl<>(total.subList((int) start, (int) end).stream().map(QuestionDTO::toDto).collect(Collectors.toList()), pageable2, total.size());
    }

    /*
    * @author: Manh Tran
    * @since: 15/06/2022 8:31 SA
    * @description-VN: Lọc câu hỏi theo trạng thái.
    * @description-EN: Filter questions by status.
    * @param: status - Trạng thái câu hỏi.
    * @return:
    *
    * */
    public Page<QuestionDTO> findAllByStatus(String status, Pageable pageable){
        return this.questionRepository.findAllByStatus(status, pageable).map(QuestionDTO::toDto);
    }

    /*
    * @author: Manh Tran
    * @since: 15/06/2022 2:34 CH
    * @description-VN: Lọc câu hỏi theo danh mục và trạng thái.
    * @description-EN: Filter questions by category and status.
    * @param:
    * @return:
    *
    * */
    public Page<QuestionDTO> findAllByManyConditions(Integer type, String status,  Long categoryId,
                                                            Long userId, String keyword, Pageable pageable){
        Pageable pageable2 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createDate").descending());
        if(type == 1){ // Lọc theo mã danh mục và trạng thái.
            return this.questionRepository.findAllByQuestionCategory_IdAndStatus(categoryId, status, pageable2).map(QuestionDTO::toDto);
        }else if(type == 2) // Lọc theo mã danh mục, trạng thái và từ khóa (tiêu đề câu hỏi).
        {
            return this.questionRepository.findAllByQuestionCategory_IdAndStatusAndTitleContaining(categoryId, status, keyword, pageable2).map(QuestionDTO::toDto);
        }else if(type == 3) // Lọc theo mã người dùng và trạng thái.
        {
            return this.questionRepository.findAllByUserEntity_IdAndStatus(userId, status, pageable2).map(QuestionDTO::toDto);
        }else if(type == 4) // Lọc theo mã người dùng, trạng thái và từ khoá.
        {
            return this.questionRepository.findAllByUserEntity_IdAndStatusAndTitleContaining(userId, status, keyword, pageable2).map(QuestionDTO::toDto);
        }
        return null;
    }

    /*
    * @author: Manh Tran
    * @since: 16/06/2022 8:04 SA
    * @description-VN: Lọc câu hỏi theo trạng thái.
    * @description-EN: Filter questions by status.
    * @param: status - Trạng thái câu hỏi.
    * @return:
    *
    * */
    public Page<QuestionDTO> filterQuestionByStatus(String status, Pageable pageable){
        Pageable pageable2 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createDate").descending());
        return this.questionRepository.findAllByStatus(status, pageable2).map(QuestionDTO::toDto);
    }
}
