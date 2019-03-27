package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.ProductAttribute;
import com.atguigu.gmall.pms.vo.PmsProductAttributeParam;
import com.atguigu.gmall.pms.vo.ProductAttrInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品属性参数表 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface ProductAttributeService extends IService<ProductAttribute> {

    /**
     * 根据分类查询属性列表或参数列表
     * @param cid
     * @param type
     * @param pageNum
     * @param pageSize
     * @return
     */
    Map<String,Object> pageProductAttribute(Long cid, Integer type, Integer pageNum, Integer pageSize);

    /**
     * 添加商品属性信息
     * @param productAttributeParam
     * @return
     */
    boolean createProductAttribute(PmsProductAttributeParam productAttributeParam);

    /**
     * 修改商品属性信息
     * @param id
     * @param productAttributeParam
     * @return
     */
    boolean updateProductAttributeById(Long id, PmsProductAttributeParam productAttributeParam);

    /**
     * 根据分类查询属性列表或参数列表
     * @param productCategoryId
     * @return
     */
    List<ProductAttrInfo> getAttrInfoList(Long productCategoryId);
}
