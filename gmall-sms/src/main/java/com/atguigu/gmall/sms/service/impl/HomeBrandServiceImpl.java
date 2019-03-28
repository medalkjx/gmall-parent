package com.atguigu.gmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.sms.entity.HomeBrand;
import com.atguigu.gmall.sms.mapper.HomeBrandMapper;
import com.atguigu.gmall.sms.service.HomeBrandService;
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
 * 首页推荐品牌表 服务实现类
 * </p>
 *
 * @author mei
 * @since 2019-03-25
 */
@Component
@Service
public class HomeBrandServiceImpl extends ServiceImpl<HomeBrandMapper, HomeBrand> implements HomeBrandService {

    @Override
    public Map<String, Object> pageHomeBrand(String brandName, Integer recommendStatus, Integer pageNum, Integer pageSize) {
        Page<HomeBrand> page = new Page<>(pageNum, pageSize);
        QueryWrapper<HomeBrand> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(brandName)) {
            queryWrapper.eq("brand_name", brandName);
        }
        if (!StringUtils.isEmpty(recommendStatus)) {
            queryWrapper.eq("recommend_status", recommendStatus);
        }
        IPage<HomeBrand> selectPage = baseMapper.selectPage(page, queryWrapper);
        Map<String, Object> map = PageMapUtils.getPageMap(selectPage);
        return map;
    }

    @Override
    public boolean createHomeBrands(List<HomeBrand> homeBrandList) {
        AtomicReference<Integer> count = new AtomicReference<Integer>(0);
        homeBrandList.forEach(homeBrand -> {
            Integer insert = baseMapper.insert(homeBrand);
            if (insert > 0) {
                count.set(count.get() + 1);
            }
        });
        if (count.get() == homeBrandList.size()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteByIds(List<Long> ids) {
        Integer delete = baseMapper.delete(new QueryWrapper<HomeBrand>().in("id", ids));
        return null != delete && delete > 0;
    }

    @Override
    public boolean updateSortById(Long id, Integer sort) {
        HomeBrand homeBrand = new HomeBrand();
        homeBrand.setId(id);
        homeBrand.setSort(sort);
        Integer update = baseMapper.updateById(homeBrand);
        return null != update && update > 0;
    }

    @Override
    public boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        HomeBrand homeBrand = new HomeBrand();
        homeBrand.setRecommendStatus(recommendStatus);
        Integer update = baseMapper.update(homeBrand, new QueryWrapper<HomeBrand>().in("id", ids));
        return null != update && update > 0;
    }
}
