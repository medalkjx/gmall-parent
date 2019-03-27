package com.atguigu.gmall.sms.mapper;

import com.atguigu.gmall.sms.entity.CouponProductRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 优惠券和产品的关系表 Mapper 接口
 * </p>
 *
 * @author mei
 * @since 2019-03-25
 */
public interface CouponProductRelationMapper extends BaseMapper<CouponProductRelation> {

    List<CouponProductRelation> selectCouponProductCategoryRelationByCouponId(Long id);

    void deleteByCouponId(Long id);
}
