package com.atguigu.gmall.sms.mapper;

import com.atguigu.gmall.sms.entity.CouponProductCategoryRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 优惠券和产品分类关系表 Mapper 接口
 * </p>
 *
 * @author mei
 * @since 2019-03-25
 */
public interface CouponProductCategoryRelationMapper extends BaseMapper<CouponProductCategoryRelation> {

    List<CouponProductCategoryRelation> selectCouponProductCategoryRelationByCouponId(Long id);

    void deleteByCouponId(Long id);
}
