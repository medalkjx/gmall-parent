package com.atguigu.gmall.sms.service;

import com.atguigu.gmall.sms.entity.FlashPromotionProductRelation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品限时购与商品关系表 服务类
 * </p>
 *
 * @author mei
 * @since 2019-03-25
 */
public interface FlashPromotionProductRelationService extends IService<FlashPromotionProductRelation> {

    /**
     * 分页查询不同场次关联及商品信息
     *
     * @param flashPromotionId
     * @param flashPromotionSessionId
     * @param pageNum
     * @param pageSize
     * @return
     */
    Map<String, Object> pageFlashPromotionProductRelation(Long flashPromotionId, Long flashPromotionSessionId, Integer pageNum, Integer pageSize);

    /**
     * 批量选择商品添加关联
     *
     * @param flashPromotionProductRelationList
     * @return
     */
    boolean create(List<FlashPromotionProductRelation> flashPromotionProductRelationList);
}
