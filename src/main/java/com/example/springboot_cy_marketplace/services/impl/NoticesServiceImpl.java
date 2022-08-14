package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.dto.NoticesLocalDTO;
import com.example.springboot_cy_marketplace.dto.UpdateNoticesLocalDTO;
import com.example.springboot_cy_marketplace.entity.NoticesLocalEntity;
import com.example.springboot_cy_marketplace.entity.NotifyCategoryEntity;
import com.example.springboot_cy_marketplace.entity.QuestionEntity;
import com.example.springboot_cy_marketplace.entity.UserEntity;
import com.example.springboot_cy_marketplace.model.NoticesLocalModel;
import com.example.springboot_cy_marketplace.repository.INoticesLocalRepository;
import com.example.springboot_cy_marketplace.repository.INotifyCategoryRepository;
import com.example.springboot_cy_marketplace.repository.IQuestionRepository;
import com.example.springboot_cy_marketplace.repository.IUserRepository;
import com.example.springboot_cy_marketplace.services.AmazonClient;
import com.example.springboot_cy_marketplace.services.INoticesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoticesServiceImpl implements INoticesService {
    @Autowired
    INoticesLocalRepository iNoticesLocalRepository;
    @Autowired
    INotifyCategoryRepository iNotifyCategoryRepository;
    @Autowired
    IUserRepository iUserRepository;
    @Autowired
    IQuestionRepository iQuestionRepository;
    @Autowired
    AmazonClient amazonClient;

    @Override
    public List<NoticesLocalModel> findAll() {
        return null;
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:23 CH
     * @description-VN:  Lấy danh sách toàn bộ thông báo
     * @description-EN:  Get a list of all notifications
     * @param: page
     * @return: Page<NoticesLocalModel>
     *
     * */
    @Override
    public Page<NoticesLocalModel> findAll(Pageable page) {
        return iNoticesLocalRepository.findAllNoticesLocal(page).map(notices -> NoticesLocalModel.entityToModel(notices));
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:23 CH
     * @description-VN:  Tìm thông báo theo id của thông báo
     * @description-EN:  Find notifications by notification id
     * @param: id
     * @return: NoticesLocalModel
     *
     * */
    @Override
    public NoticesLocalModel findById(Long id) {
        return NoticesLocalModel.entityToModel(iNoticesLocalRepository.findById(id).get());
    }

    @Override
    public Page<NoticesLocalModel> findAllOwnerNotices(Long id, Pageable pageable){
        return iNoticesLocalRepository.findAllOwnerNotices(id,pageable).map(notices -> NoticesLocalModel.entityToModel(notices));
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:25 CH
     * @description-VN:  Thêm mới thông báo
     * @description-EN:  Add new notification
     * @param: dto
     * @return: NoticesLocalModel
     *
     * */
    @Override
    public NoticesLocalModel add(NoticesLocalDTO dto) {
        NoticesLocalEntity noticesLocalEntity = NoticesLocalDTO.dtoToEntity(dto);
        if (dto.getIdUser() != null){
            noticesLocalEntity.setUserEntity(iUserRepository.findById(dto.getIdUser()).get());
        }
        iNoticesLocalRepository.save(noticesLocalEntity);

        return NoticesLocalModel.entityToModel(noticesLocalEntity);
    }

    @Override
    public List<NoticesLocalModel> add(List<NoticesLocalDTO> dto) {
        return null;
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:26 CH
     * @description-VN:  Sửa nội dung thông báo
     * @description-EN:  Edit notification content
     * @param: dto
     * @return: NoticesLocalModel
     *
     * */
    @Override
    public NoticesLocalModel update(NoticesLocalDTO dto) {
        NoticesLocalEntity noticesLocalEntity = NoticesLocalDTO.dtoToEntity(dto);
        noticesLocalEntity.setUserEntity(iUserRepository.findById(dto.getIdUser()).get());
        iNoticesLocalRepository.save(noticesLocalEntity);
        return NoticesLocalModel.entityToModel(noticesLocalEntity);


    }

    /*
     * @author: HaiPhong
     * @since: 15/06/2022 1:39 CH
     * @description-VN:  Chỉnh sửa thông báo đến tất cả người dùng
     * @description-EN:  Update notices local
     * @param: updateNoticesLocalDTO
     * @return: NoticesLocalModel
     *
     * */
    @Override
    public NoticesLocalModel updateNoticesLocal(UpdateNoticesLocalDTO updateNoticesLocalDTO) {
        Optional<NoticesLocalEntity> optionalQuestionEntity = this.iNoticesLocalRepository.findById(updateNoticesLocalDTO.getId());
        if (!optionalQuestionEntity.isPresent()) {
            return null;
        }
        NoticesLocalModel model = new NoticesLocalModel();
        model.setId(updateNoticesLocalDTO.getId());
        model.setTitle(updateNoticesLocalDTO.getTitle());
        model.setContent(updateNoticesLocalDTO.getContent());

        if (updateNoticesLocalDTO.getOldImageIds() != null && updateNoticesLocalDTO.getOldImageIds().size() > 0) {
            if (updateNoticesLocalDTO.getOldImageIds().contains(1L)) {
                model.setImg1(optionalQuestionEntity.get().getImg1());
            }
            if (updateNoticesLocalDTO.getOldImageIds().contains(2L)) {
                model.setImg2(optionalQuestionEntity.get().getImg2());
            }
            if (updateNoticesLocalDTO.getOldImageIds().contains(3L)) {
                model.setImg3(optionalQuestionEntity.get().getImg3());
            }
        }
        if (updateNoticesLocalDTO.getImg() != null) {
            for (MultipartFile file : updateNoticesLocalDTO.getImg()) {
                if (model.getImg1() == null) {
                    if (optionalQuestionEntity.get().getImg1() != null) {
                        //    amazonClient.deleteFileFromS3Bucket(optionalQuestionEntity.get().getImg1());
                    }
                    model.setImg1(amazonClient.uploadFilewithFolder(file, "notification"));
                } else if (model.getImg2() == null) {
                    if (optionalQuestionEntity.get().getImg2() != null) {
                        //   amazonClient.deleteFileFromS3Bucket(optionalQuestionEntity.get().getImg2());
                    }
                    model.setImg2(amazonClient.uploadFilewithFolder(file, "notification"));
                } else if (model.getImg3() == null) {
                    if (optionalQuestionEntity.get().getImg3() != null) {
                        //   amazonClient.deleteFileFromS3Bucket(optionalQuestionEntity.get().getImg3());
                    }
                    model.setImg3(amazonClient.uploadFilewithFolder(file, "notification"));
                }
            }
        }
        NoticesLocalEntity noticesLocalEntity = NoticesLocalModel.modelToEntity(model);
        noticesLocalEntity.setUserEntity(optionalQuestionEntity.get().getUserEntity());
        iNoticesLocalRepository.save(noticesLocalEntity);
        return model;
    }

    @Override
    public void addNoticesToUser(Long idUserAnswer, String contentQuestion, String time, Long idUserAddQuestion) {
        NotifyCategoryEntity notifyCategoryEntity = iNotifyCategoryRepository.findByName("Thông báo hệ thống");
        QuestionEntity questionEntity = iQuestionRepository.findById(idUserAddQuestion).get();
        UserEntity userEntity = iUserRepository.findById(idUserAnswer).get();
        iNoticesLocalRepository.save(new NoticesLocalEntity(null,
                 "Admin "+userEntity.getFullName() + " đã trả lời câu hỏi \""+ contentQuestion + "\" của bạn vào lúc " + time,
                null,questionEntity.getUserEntity().getAvatar(),null,null,questionEntity.getUserEntity(),notifyCategoryEntity));
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:27 CH
     * @description-VN:  Xóa thông báo
     * @description-EN:  Delete notification
     * @param: id
     * @return:
     *
     * */
    @Override
    public boolean deleteById(Long id) {
        if (id != null) {
            iNoticesLocalRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }


    /*
     * @author: HieuMM
     * @since: 20-Jun-22 9:46 AM
     * @description-VN: Lấy thông báo riêng cho người dùng săp xếp theo thời gian
     * @description-EN:  Get all notification for every user desc datetime modifi
     * @param:
     * */
    public List<NoticesLocalDTO> getNoticesForUserDescTime(Long userId) {
        List<NoticesLocalModel> models =  iNoticesLocalRepository.findAllByUserEntity_IdAndDescTime(userId).stream().map((e) -> NoticesLocalModel.entityToModel(e)).collect(Collectors.toList());
        return models.stream().map((model) -> NoticesLocalDTO.modelToDTO(model)).collect(Collectors.toList());
    }

    @Override
    public boolean deleteByIds(List<Long> id) {
        return false;
    }
}
