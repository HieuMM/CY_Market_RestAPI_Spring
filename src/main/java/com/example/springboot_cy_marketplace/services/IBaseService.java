package com.example.springboot_cy_marketplace.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;

public interface IBaseService <M,D,L>{
    List<M> findAll();

    Page<M> findAll(Pageable page);

    M findById(L id);

    M add(D dto);

    List<M> add(List<D> dto);

    M update(D dto);

    boolean deleteById(L id);

    boolean deleteByIds(List<L> id);
}
