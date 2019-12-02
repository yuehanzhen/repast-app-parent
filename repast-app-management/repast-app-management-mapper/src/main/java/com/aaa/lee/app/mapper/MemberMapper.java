package com.aaa.lee.app.mapper;

import com.aaa.lee.app.domain.Member;
import tk.mybatis.mapper.common.Mapper;

public interface MemberMapper extends Mapper<Member> {
    /**
     * 检查该token是否存在，用户身份是否过期
     * @param token
     * @return
     */
    Member getMemberByToken(String token);
}