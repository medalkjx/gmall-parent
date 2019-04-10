package com.atguigu.gmall.sms.vo;

import com.atguigu.gmall.pms.entity.Product;
import com.atguigu.gmall.sms.entity.FlashPromotionProductRelation;
import lombok.Data;

import java.util.List;

/**
 * @author ：mei
 * @date ：Created in 2019/3/29 0029 上午 8:47
 * @description：场次关联及商品信息
 * @modified By：
 * @version: $
 */
@Data
public class FlashPromotionProductRelationParam extends FlashPromotionProductRelation {
    private Product product;
}
