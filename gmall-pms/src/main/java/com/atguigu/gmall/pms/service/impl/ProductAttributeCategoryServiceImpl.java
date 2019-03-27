package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.pms.entity.ProductAttribute;
import com.atguigu.gmall.pms.entity.ProductAttributeCategory;
import com.atguigu.gmall.pms.mapper.ProductAttributeCategoryMapper;
import com.atguigu.gmall.pms.mapper.ProductAttributeMapper;
import com.atguigu.gmall.pms.service.ProductAttributeCategoryService;
import com.atguigu.gmall.pms.vo.PmsProductAttributeCategoryItem;
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
 * 产品属性分类表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Service
@Component
public class ProductAttributeCategoryServiceImpl extends ServiceImpl<ProductAttributeCategoryMapper, ProductAttributeCategory> implements ProductAttributeCategoryService {

    @Autowired
    private ProductAttributeMapper productAttributeMapper;

    @Override
    public Map<String, Object> pageProductAttributeCategory(Integer pageNum, Integer pageSize) {
        ProductAttributeCategoryMapper productAttributeCategoryMapper = getBaseMapper();
        Page<ProductAttributeCategory> page = new Page<>(pageNum, pageSize);
        IPage<ProductAttributeCategory> pageInfo = productAttributeCategoryMapper.selectPage(page, null);
        Map<String, Object> result = PageMapUtils.getPageMap(pageInfo);
        return result;
    }

    @Override
    public boolean createProductAttributeCategory(String name) {
        ProductAttributeCategory productAttributeCategory = new ProductAttributeCategory();
        productAttributeCategory.setName(name);
        Integer result = baseMapper.insert(productAttributeCategory);
        return null != result && result > 0;
    }

    @Override
    public boolean updateProductAttributeCategoryById(Long id, String name) {
        ProductAttributeCategory productAttributeCategory = new ProductAttributeCategory();
        productAttributeCategory.setId(id);
        productAttributeCategory.setName(name);
        Integer result = baseMapper.updateById(productAttributeCategory);
        return null != result && result > 0;
    }

    @Override
    public List<PmsProductAttributeCategoryItem> getProductAttributeCategoryList() {
        ArrayList<PmsProductAttributeCategoryItem> items = new ArrayList<>();
        List<ProductAttributeCategory> productAttributeCategoryList = baseMapper.selectList(null);
        for (ProductAttributeCategory productAttributeCategory : productAttributeCategoryList) {
            PmsProductAttributeCategoryItem pmsProductAttributeCategoryItem = new PmsProductAttributeCategoryItem();
            BeanUtils.copyProperties(productAttributeCategory,pmsProductAttributeCategoryItem);
            Long id = productAttributeCategory.getId();
            QueryWrapper<ProductAttribute> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("product_attribute_category_id", id);
            List<ProductAttribute> productAttributeList = productAttributeMapper.selectList(queryWrapper);
            pmsProductAttributeCategoryItem.setProductAttributeList(productAttributeList);
            items.add(pmsProductAttributeCategoryItem);
        }
        return items;
    }


}
