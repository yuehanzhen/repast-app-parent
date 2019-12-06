package com.aaa.lee.app.mapper;

import com.aaa.lee.app.domain.PCom;
import tk.mybatis.mapper.common.Mapper;

public interface PComMapper extends Mapper<PCom> {
    PCom selectPcomById(Long productId);
}