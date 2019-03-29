package com.atguigu.gmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.sms.entity.HomeRecommendProduct;
import com.atguigu.gmall.sms.mapper.HomeRecommendProductMapper;
import com.atguigu.gmall.sms.service.HomeRecommendProductService;
import com.atguigu.gmall.utils.PageMapUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 人气推荐商品表 服务实现类
 * </p>
 *
 * @author mei
 * @since 2019-03-25
 */
@Service
@Component
public class HomeRecommendProductServiceImpl extends ServiceImpl<HomeRecommendProductMapper, HomeRecommendProduct> implements HomeRecommendProductService {

    @Override
    public Map<String, Object> pageHomeRecommendProduct(String productName, Integer recommendStatus, Integer pageNum, Integer pageSize) {
        Page<HomeRecommendProduct> page = new Page<>(pageNum, pageSize);
        QueryWrapper<HomeRecommendProduct> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(productName)) {
            queryWrapper.like("product_name", productName);
        }
        if (!StringUtils.isEmpty(recommendStatus)) {
            queryWrapper.eq("recommend_status", recommendStatus);
        }
        IPage<HomeRecommendProduct> selectPage = baseMapper.selectPage(page, queryWrapper);
        Map<String, Object> pageMap = PageMapUtils.getPageMap(selectPage);
        return pageMap;
    }

    @Override
    public boolean create(List<HomeRecommendProduct> homeRecommendProductList) {
        AtomicReference<Integer> count = new AtomicReference<>(0);
        homeRecommendProductList.forEach(homeRecommendProduct -> {
            Integer insert = baseMapper.insert(homeRecommendProduct);
            if (null != insert && insert > 0) {
                count.set(count.get() + 1);
            }
        });
        if (count.get() == homeRecommendProductList.size()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(List<Long> ids) {
        Integer delete = baseMapper.delete(new QueryWrapper<HomeRecommendProduct>().in("id", ids));
        return null != delete && delete > 0;
    }

    @Override
    public boolean updateRecommendStatus(Integer recommendStatus, List<Long> ids) {
        HomeRecommendProduct homeRecommendProduct = new HomeRecommendProduct();
        homeRecommendProduct.setRecommendStatus(recommendStatus);
        Integer update = baseMapper.update(homeRecommendProduct, new QueryWrapper<HomeRecommendProduct>().in("id", ids));
        return null != update && update > 0;


    }

    @Override
    public boolean updateSort(Long id, Integer sort) {
        HomeRecommendProduct homeRecommendProduct = new HomeRecommendProduct();
        homeRecommendProduct.setId(id);
        homeRecommendProduct.setSort(sort);
        Integer update = baseMapper.updateById(homeRecommendProduct);
        return null != update && update > 0;
    }


}
