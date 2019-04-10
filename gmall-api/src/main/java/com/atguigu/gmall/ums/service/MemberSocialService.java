package com.atguigu.gmall.ums.service;

import com.atguigu.gmall.cas.vo.AccessTokenVo;
import com.atguigu.gmall.ums.entity.Member;
import com.atguigu.gmall.ums.entity.MemberSocial;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author mei
 * @since 2019-04-01
 */
public interface MemberSocialService extends IService<MemberSocial> {

    /**
     * 通过token获取用户信息
     *
     * @param tokenVo
     * @return
     */
    Member getMemberInfo(AccessTokenVo tokenVo);
}
