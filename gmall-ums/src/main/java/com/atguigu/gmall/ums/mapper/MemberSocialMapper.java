package com.atguigu.gmall.ums.mapper;

import com.atguigu.gmall.ums.entity.Member;
import com.atguigu.gmall.ums.entity.MemberSocial;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author mei
 * @since 2019-04-01
 */
public interface MemberSocialMapper extends BaseMapper<MemberSocial> {

    Member getMemberInfo(String uid);

    List<MemberSocial> selectAccessTokenForUpdate(String access_token);
}
