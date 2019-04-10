package com.atguigu.gmall.ums.service;

import com.atguigu.gmall.ums.entity.MemberReceiveAddress;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 会员收货地址表 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface MemberReceiveAddressService extends IService<MemberReceiveAddress> {

    /**
     * 根据用户id获取用户地址
     * @param id
     * @return
     */
    List<MemberReceiveAddress> selectMemberReceiveAddressByMemberId(Long id);
}
