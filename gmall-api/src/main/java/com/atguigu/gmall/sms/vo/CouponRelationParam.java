package com.atguigu.gmall.sms.vo;

import com.atguigu.gmall.sms.entity.Coupon;
import com.atguigu.gmall.sms.entity.CouponProductCategoryRelation;
import com.atguigu.gmall.sms.entity.CouponProductRelation;
import lombok.Data;

import java.util.List;

/**
 * @author ：mei
 * @date ：Created in 2019/3/25 0025 上午 10:18
 * @description：单个优惠券的详细信息
 * @modified By：
 * @version: $
 */
@Data
public class CouponRelationParam extends Coupon {
    private List<CouponProductRelation> couponProductRelationList;
    private List<CouponProductCategoryRelation> couponProductCategoryRelationList;
}
