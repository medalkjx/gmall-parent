package com.atguigu.gmall.sms.service.impl;

import com.atguigu.gmall.sms.entity.HomeNewProduct;
import com.atguigu.gmall.sms.mapper.HomeNewProductMapper;
import com.atguigu.gmall.sms.service.HomeNewProductService;
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
 * 新鲜好物表 服务实现类
 * </p>
 *
 * @author mei
 * @since 2019-03-25
 */
@Component
public class HomeNewProductServiceImpl extends ServiceImpl<HomeNewProductMapper, HomeNewProduct> implements HomeNewProductService {

    @Override
    public Map<String, Object> pageHomeNewProduct(String productName, Integer recommendStatus, Integer pageNum, Integer pageSize) {
        Page<HomeNewProduct> page = new Page<>(pageNum, pageSize);
        QueryWrapper<HomeNewProduct> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(productName)) {
            queryWrapper.like("product_name", productName);
        }
        if (!StringUtils.isEmpty(recommendStatus)) {
            queryWrapper.eq("recommend_status", recommendStatus);
        }
        IPage<HomeNewProduct> selectPage = baseMapper.selectPage(page, queryWrapper);
        Map<String, Object> pageMap = PageMapUtils.getPageMap(selectPage);
        return pageMap;
    }

    @Override
    public boolean create(List<HomeNewProduct> list) {
        AtomicReference<Integer> count = new AtomicReference<>(0);
        list.forEach(homeNewProduct -> {
            Integer insert = baseMapper.insert(homeNewProduct);
            count.set(count.get() + 1);
        });
        if (count.get() == list.size()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteByIds(List<Long> ids) {
        Integer delete = baseMapper.deleteBatchIds(ids);
        return null != delete && delete > 0;
    }

    @Override
    public boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        HomeNewProduct homeNewProduct = new HomeNewProduct();
        homeNewProduct.setRecommendStatus(recommendStatus);
        Integer update = baseMapper.update(homeNewProduct, new QueryWrapper<HomeNewProduct>().in("id", ids));
        return null != update && update > 0;
    }

    @Override
    public boolean updateSortById(Integer sort, Long id) {
        HomeNewProduct homeNewProduct = new HomeNewProduct();
        homeNewProduct.setId(id);
        homeNewProduct.setSort(sort);
        Integer update = baseMapper.updateById(homeNewProduct);
        return null != update && update > 0;
    }
}
