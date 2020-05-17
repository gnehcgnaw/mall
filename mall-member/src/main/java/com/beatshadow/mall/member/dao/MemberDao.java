package com.beatshadow.mall.member.dao;

import com.beatshadow.mall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author gnehcgnaw
 * @email gnehcgnaw@gmail.com
 * @date 2020-05-18 06:58:09
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
