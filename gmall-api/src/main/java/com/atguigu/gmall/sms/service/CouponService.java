package com.atguigu.gmall.sms.service;

import com.atguigu.gmall.sms.entity.Coupon;
import com.atguigu.gmall.sms.vo.CouponRelationParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 优惠卷表 服务类
 * </p>
 *
 * @author mei
 * @since 2019-03-25
 */
public interface CouponService extends IService<Coupon> {

    /**
     * 根据优惠券名称和类型分页获取优惠券列表
     * @param name
     * @param type
     * @param pageNum
     * @param pageSize
     * @return
     */
    Map<String, Object> pageCoupon(String name, Integer type, Integer pageNum, Integer pageSize);

    /**
     * 获取单个优惠券的详细信息
     * @param id
     * @return
     */
    CouponRelationParam getCouponRelationParam(Long id);

    /**
     * 添加优惠卷
     * @param couponRelationParam
     */
    void createCouponRelationParam(CouponRelationParam couponRelationParam);

    /**
     * 删除优惠券
     * @param id
     */
    void deleteById(Long id);

    /**
     * 修改优惠券
     * @param couponRelationParam
     * @param id
     */
    void updateCouponInfoById(CouponRelationParam couponRelationParam,Long id);
}
