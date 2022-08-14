package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.dto.AnswerDTO;
import com.example.springboot_cy_marketplace.dto.MailDTO;
import com.example.springboot_cy_marketplace.model.AnswerModel;
import com.example.springboot_cy_marketplace.model.Constant;
import com.example.springboot_cy_marketplace.repository.*;
import com.example.springboot_cy_marketplace.services.IAnswerService;
import com.example.springboot_cy_marketplace.services.INoticesService;
import com.example.springboot_cy_marketplace.entity.AnswerEntity;
import com.example.springboot_cy_marketplace.entity.QuestionEntity;
import com.example.springboot_cy_marketplace.entity.RoleEntity;
import com.example.springboot_cy_marketplace.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnswerServieceImpl implements IAnswerService {
    @Autowired
    IAnswerRepository iAnswerRepository;
    @Autowired
    IQuestionRepository iQuestionRepository;

    @Autowired
    IUserRepository iUserRepository;
    @Autowired
    MailService mailService;
    @Autowired
    IRoleRepository roleRepository;
    @Autowired
    INotifyCategoryRepository iNotifyCategoryRepository;
    @Value("${server.host.fe.user}")
    private String feHost;
    @Autowired
    INoticesService iNoticesService;

    @Override
    public List<AnswerModel> findAll() {
        return null;
    }

    @Override
    public Page<AnswerModel> findAll(Pageable page) {
        return null;
    }

    @Override
    public AnswerModel findById(Long id) {
        return null;
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:16 CH
     * @description-VN:  Thêm mới câu trả lời cho câu hỏi, nếu người thêm là admin thì thay đổi trạng thái của câu hỏi
     * @description-EN:  Add a new answer to the question, if the person adding is admin, change the status of the question
     * @param: model
     * @return: AnswerModel
     *
     * */
    @Override
    public AnswerModel add(AnswerDTO model) {
        AnswerEntity answerEntity = AnswerDTO.dtoToEntity(model);
        QuestionEntity questionEntity = iQuestionRepository.findById(model.getIdQuestion()).get();
        RoleEntity roleEntity = roleRepository.findById(3L).get(); //ROLE_ADMIN
        UserEntity userEntity = iUserRepository.findById(model.getUserDTO().getId()).get();
        if (userEntity.getRoleEntityList().contains(roleEntity)) {
            questionEntity.setStatus(Constant.QUESTION_ANSWERED);

            answerEntity.setQuestionEntity(questionEntity);
            answerEntity.setUserEntity(userEntity);
            AnswerEntity answer = iAnswerRepository.save(answerEntity);

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
            iNoticesService.addNoticesToUser(userEntity.getId(), questionEntity.getTitle(), sdf.format(answer.getModifiedDate()), questionEntity.getId());
            this.sendMailNoticesAnswered(answer);
        }else {
            answerEntity.setQuestionEntity(questionEntity);
            answerEntity.setUserEntity(userEntity);
            AnswerEntity answer = iAnswerRepository.save(answerEntity);
        }
//        answerEntity.setQuestionEntity(questionEntity);
//        answerEntity.setUserEntity(userEntity);
//        AnswerEntity answer = iAnswerRepository.save(answerEntity);
//
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
//        iNoticesService.addNoticesToUser(userEntity.getId(), questionEntity.getTitle(), sdf.format(answer.getModifiedDate()), questionEntity.getId());
//        this.sendMailNoticesAnswered(answer);

        return AnswerModel.entityToModel(answerEntity);

    }

    @Override
    public List<AnswerModel> add(List<AnswerDTO> model) {
        return null;
    }

    @Override
    public AnswerModel update(AnswerDTO model) {
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
     * @since: 10/06/2022 4:17 CH
     * @description-VN:  Lấy danh sách tất cả câu trả lời của một câu hỏi theo id của câu hỏi
     * @description-EN:  Get a list of all the answers of a question by the question id
     * @param: id_question, pageable
     * @return: Page<AnswerModel>
     *
     * */
    @Override
    public Page<AnswerModel> getAllByIdQuestion(Long id_question, Pageable pageable) {
        return iAnswerRepository.findAllByQuestionEntity(iQuestionRepository.findById(id_question).get(), pageable).map(ans -> AnswerModel.entityToModel(ans));
    }

    public void sendMailNoticesAnswered(AnswerEntity answerEntity){
        UserEntity userAnswer = answerEntity.getUserEntity();
        QuestionEntity questionEntity = answerEntity.getQuestionEntity();
        UserEntity userCreateAnswer = questionEntity.getUserEntity();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        MailDTO mail = new MailDTO();
        mail.setFrom("Trần Hoàng Mạnh");
        mail.setMailTo(userCreateAnswer.getEmail());
        mail.setSubject("Thông báo !");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("url", feHost+"/question-detail/"+questionEntity.getId());
        model.put("title", questionEntity.getTitle());
        model.put("idQuestion", questionEntity.getId());
        model.put("userAnswer", userAnswer.getFullName());
        model.put("userCreateAnswer", userCreateAnswer.getFullName());
        model.put("createDate", sdf.format(answerEntity.getCreateDate()));

        mail.setProps(model);
        try {
            this.mailService.sendMailNoticesAnswered(mail);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
