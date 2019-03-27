package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.pms.entity.ProductAttribute;
import com.atguigu.gmall.pms.entity.ProductAttributeValue;
import com.atguigu.gmall.pms.mapper.ProductAttributeMapper;
import com.atguigu.gmall.pms.mapper.ProductAttributeValueMapper;
import com.atguigu.gmall.pms.service.ProductAttributeService;
import com.atguigu.gmall.pms.vo.PmsProductAttributeParam;
import com.atguigu.gmall.pms.vo.ProductAttrInfo;
import com.atguigu.gmall.utils.PageMapUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品属性参数表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Service
@Component
public class ProductAttributeServiceImpl extends ServiceImpl<ProductAttributeMapper, ProductAttribute> implements ProductAttributeService {

    @Autowired
    private ProductAttributeValueMapper productAttributeValueMapper;

    @Override
    public Map<String, Object> pageProductAttribute(Long cid, Integer type, Integer pageNum, Integer pageSize) {
        Page<ProductAttribute> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ProductAttribute> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_attribute_category_id", cid)
                .eq("type", type);
        IPage<ProductAttribute> pageInfo = baseMapper.selectPage(page, queryWrapper);
        Map<String, Object> result = PageMapUtils.getPageMap(pageInfo);
        return result;
    }

    @Override
    public boolean createProductAttribute(PmsProductAttributeParam productAttributeParam) {
        ProductAttribute productAttribute = new ProductAttribute();
        BeanUtils.copyProperties(productAttributeParam, productAttribute);
        Integer result = baseMapper.insert(productAttribute);
        return null != result && result > 0;
    }

    @Override
    public boolean updateProductAttributeById(Long id, PmsProductAttributeParam productAttributeParam) {
        ProductAttribute productAttribute = new ProductAttribute();
        BeanUtils.copyProperties(productAttributeParam,productAttribute);
        productAttribute.setId(id);
        Integer result = baseMapper.updateById(productAttribute);
        return null != result && result > 0;
    }

    @Override
    public List<ProductAttrInfo> getAttrInfoList(Long productCategoryId) {
        List<ProductAttrInfo> productAttrInfoList = new ArrayList<>();
        List<ProductAttributeValue> productAttributeValueList = productAttributeValueMapper.selectList(new QueryWrapper<ProductAttributeValue>().eq("product_id", productCategoryId));
        for (ProductAttributeValue productAttributeValue : productAttributeValueList) {
            Long productAttributeId = productAttributeValue.getProductAttributeId();
            List<ProductAttribute> productAttributeList = baseMapper.selectList(new QueryWrapper<ProductAttribute>().eq("product_attribute_category_id", productAttributeId));
            for (ProductAttribute productAttribute : productAttributeList) {
                ProductAttrInfo productAttrInfo = new ProductAttrInfo();
                Long id = productAttribute.getId();
                productAttrInfo.setAttributeId(id);
                productAttrInfo.setAttributeCategoryId(productAttributeId);
                productAttrInfoList.add(productAttrInfo);
            }
        }
        return productAttrInfoList;
    }
}
