package com.atguigu.gmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.sms.entity.HomeAdvertise;
import com.atguigu.gmall.sms.mapper.HomeAdvertiseMapper;
import com.atguigu.gmall.sms.service.HomeAdvertiseService;
import com.atguigu.gmall.utils.PageMapUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * <p>
 * 首页轮播广告表 服务实现类
 * </p>
 *
 * @author mei
 * @since 2019-03-25
 */
@Component
@Service
public class HomeAdvertiseServiceImpl extends ServiceImpl<HomeAdvertiseMapper, HomeAdvertise> implements HomeAdvertiseService {

    @Override
    public Map<String, Object> pageHomeAdvertise(String name, Integer type, String endTime, Integer pageNum, Integer pageSize) {
        Page<HomeAdvertise> page = new Page<>(pageNum, pageSize);
        QueryWrapper<HomeAdvertise> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(type)) {
            queryWrapper.eq("type", type);
        }
        if (!StringUtils.isEmpty(endTime)) {
            queryWrapper.eq("end_time", endTime);
        }
        IPage<HomeAdvertise> selectPage = baseMapper.selectPage(page, queryWrapper);
        Map<String, Object> map = PageMapUtils.getPageMap(selectPage);
        return map;
    }

    @Override
    public boolean updateStatus(Integer status, Long id) {
        HomeAdvertise homeAdvertise = new HomeAdvertise();
        homeAdvertise.setId(id);
        homeAdvertise.setStatus(status);
        Integer update = baseMapper.updateById(homeAdvertise);
        return null != update && update > 0;
    }
}
