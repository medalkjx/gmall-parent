package com.atguigu.gmall.sms.service;

import com.atguigu.gmall.sms.entity.CouponHistory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 优惠券使用、领取历史表 服务类
 * </p>
 *
 * @author mei
 * @since 2019-03-25
 */
public interface CouponHistoryService extends IService<CouponHistory> {

    /**
     * 根据条件查询优惠券领取记录
     * @param couponId
     * @param useStatus
     * @param orderSn
     * @param pageNum
     * @param pageSize
     * @return
     */
    Map<String,Object> pageCouponHistory(Long couponId, Integer useStatus, String orderSn, Integer pageNum, Integer pageSize);
}
