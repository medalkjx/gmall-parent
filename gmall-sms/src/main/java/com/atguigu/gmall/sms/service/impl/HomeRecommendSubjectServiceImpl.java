package com.atguigu.gmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.sms.entity.HomeRecommendSubject;
import com.atguigu.gmall.sms.mapper.HomeRecommendSubjectMapper;
import com.atguigu.gmall.sms.service.HomeRecommendSubjectService;
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
 * 首页推荐专题表 服务实现类
 * </p>
 *
 * @author mei
 * @since 2019-03-25
 */
@Component
@Service
public class HomeRecommendSubjectServiceImpl extends ServiceImpl<HomeRecommendSubjectMapper, HomeRecommendSubject> implements HomeRecommendSubjectService {

    @Override
    public Map<String, Object> pageHomeRecommendSubject(String subjectName, Integer recommendStatus, Integer pageNum, Integer pageSize) {
        Page<HomeRecommendSubject> page = new Page<>(pageNum, pageSize);
        QueryWrapper<HomeRecommendSubject> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(subjectName)) {
            queryWrapper.like("subject_name", subjectName);
        }
        if (!StringUtils.isEmpty(recommendStatus)) {
            queryWrapper.eq("recommend_status", recommendStatus);
        }
        IPage<HomeRecommendSubject> selectPage = baseMapper.selectPage(page, queryWrapper);
        Map<String, Object> pageMap = PageMapUtils.getPageMap(selectPage);
        return pageMap;
    }

    @Override
    public boolean create(List<HomeRecommendSubject> homeRecommendSubjectList) {
        AtomicReference<Integer> count = new AtomicReference<>(0);
        homeRecommendSubjectList.forEach(homeRecommendSubject -> {
            Integer insert = baseMapper.insert(homeRecommendSubject);
            count.set(count.get() + 1);
        });
        if (count.get() == homeRecommendSubjectList.size()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(List<Long> ids) {
        Integer delete = baseMapper.deleteBatchIds(ids);
        return null != delete && delete > 0;
    }

    @Override
    public boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        HomeRecommendSubject homeRecommendSubject = new HomeRecommendSubject();
        homeRecommendSubject.setRecommendStatus(recommendStatus);
        Integer update = baseMapper.update(homeRecommendSubject, new QueryWrapper<HomeRecommendSubject>().in("id", ids));
        return null != update && update > 0;
    }

    @Override
    public boolean updateSort(Long id, Integer sort) {
        HomeRecommendSubject homeRecommendSubject = new HomeRecommendSubject();
        homeRecommendSubject.setId(id);
        homeRecommendSubject.setSort(sort);
        Integer update = baseMapper.updateById(homeRecommendSubject);
        return null != update && update > 0;
    }


}
