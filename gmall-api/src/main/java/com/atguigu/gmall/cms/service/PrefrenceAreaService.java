package com.atguigu.gmall.cms.service;

import com.atguigu.gmall.cms.entity.PrefrenceArea;
import com.atguigu.gmall.cms.entity.PrefrenceAreaProductRelation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 优选专区 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface PrefrenceAreaService extends IService<PrefrenceArea> {

    /**
     * 获取所有商品优选
     * @return
     */
    List<PrefrenceArea> getPrefrenceAreaList();

    /**
     * 根据商品id获取商品优选
     * @param productId
     * @return
     */
    List<PrefrenceAreaProductRelation> getPrefrenceAreaProductRelationListByProductId(Long productId);

    /**
     * 更新优选专区和商品的关系
     * @param prefrenceAreaProductRelationList
     * @return
     */
    void updatePreFrenceAreaProductRelationList(List<PrefrenceAreaProductRelation> prefrenceAreaProductRelationList);

    /**
     * 批量插入商品优选
     * @param prefrenceAreaProductRelationList
     * @param id
     */
    void savePrefrenceAreaProductRelationList(List<PrefrenceAreaProductRelation> prefrenceAreaProductRelationList, Long id);

}
