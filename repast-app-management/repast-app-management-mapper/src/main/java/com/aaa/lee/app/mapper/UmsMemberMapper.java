package com.aaa.lee.app.mapper;

import com.aaa.lee.app.domain.UmsMember;

import java.util.List;

public interface UmsMemberMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UmsMember record);

    UmsMember selectByPrimaryKey(Long id);

    List<UmsMember> selectAll();

    int updateByPrimaryKey(UmsMember record);

    Long getMemberId(String token);

}