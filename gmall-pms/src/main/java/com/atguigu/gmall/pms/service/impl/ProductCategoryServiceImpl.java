package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.pms.constant.RedisCacheConstant;
import com.atguigu.gmall.pms.entity.ProductCategory;
import com.atguigu.gmall.pms.mapper.ProductCategoryMapper;
import com.atguigu.gmall.pms.service.ProductCategoryService;
import com.atguigu.gmall.pms.vo.PmsProductCategoryParam;
import com.atguigu.gmall.pms.vo.PmsProductCategoryWithChildrenItem;
import com.atguigu.gmall.utils.PageMapUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 产品分类 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Service
@Component
@Slf4j
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public Map<String, Object> pageProductCategory(Integer pageNum, Integer pageSize, Long parentId) {
        ProductCategoryMapper productCategoryMapper = getBaseMapper();
        Page<ProductCategory> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ProductCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId);
        IPage<ProductCategory> pageInfo = productCategoryMapper.selectPage(page, queryWrapper);
        Map<String, Object> result = PageMapUtils.getPageMap(pageInfo);
        return result;
    }

    @Override
    public boolean createProductCategory(PmsProductCategoryParam productCategoryParam) {
        ProductCategory productCategory = new ProductCategory();
        BeanUtils.copyProperties(productCategoryParam, productCategory);
        Integer result = baseMapper.insert(productCategory);
        return null != result && result > 0;
    }

    @Override
    public boolean updateProductCategoryById(Long id, PmsProductCategoryParam productCategoryParam) {
        ProductCategory productCategory = new ProductCategory();
        BeanUtils.copyProperties(productCategoryParam, productCategory);
        productCategory.setId(id);
        Integer result = baseMapper.updateById(productCategory);
        return null != result && result > 0;
    }

    @Override
    public boolean updateNavStatusByIds(List<Long> ids, Integer navStatus) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setNavStatus(navStatus);
        QueryWrapper<ProductCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        Integer result = baseMapper.update(productCategory, queryWrapper);
        return null != result && result > 0;
    }

    @Override
    public boolean updateShowStatusByIds(List<Long> ids, Integer showStatus) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setShowStatus(showStatus);
        QueryWrapper<ProductCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        Integer result = baseMapper.update(productCategory, queryWrapper);
        return null != result && result > 0;
    }

    @Override
    public List<PmsProductCategoryWithChildrenItem> listWithChildren() {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String cache = ops.get(RedisCacheConstant.PRODUCT_CATEGORY_CACHE_KEY);
        if (!StringUtils.isEmpty(cache)){
            List<PmsProductCategoryWithChildrenItem> items = JSON.parseArray(cache, PmsProductCategoryWithChildrenItem.class);
            return items;
        }
        List<PmsProductCategoryWithChildrenItem> items = baseMapper.listWithChildren(0L);
        String jsonString = JSON.toJSONString(items);
        ops.set(RedisCacheConstant.PRODUCT_CATEGORY_CACHE_KEY,jsonString,3, TimeUnit.DAYS);
        return items;
    }

    @Override
    public boolean deleteProductCategoryById(Long id) {
        Integer result = baseMapper.deleteById(id);
        return null != result && result > 0;
    }
}
