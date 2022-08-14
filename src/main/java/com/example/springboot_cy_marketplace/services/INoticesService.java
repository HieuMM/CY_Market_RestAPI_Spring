package com.example.springboot_cy_marketplace.services;

import com.example.springboot_cy_marketplace.dto.NoticesLocalDTO;
import com.example.springboot_cy_marketplace.dto.UpdateNoticesLocalDTO;
import com.example.springboot_cy_marketplace.model.NoticesLocalModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface INoticesService extends IBaseService<NoticesLocalModel, NoticesLocalDTO,Long> {
    NoticesLocalModel updateNoticesLocal(UpdateNoticesLocalDTO updateNoticesLocalDTO);

    void addNoticesToUser(Long idUserAnswer, String contentQuestion, String time, Long idUserAddQuestion);

    Page<NoticesLocalModel> findAllOwnerNotices(Long id, Pageable pageable);
}
