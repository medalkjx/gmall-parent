package com.atguigu.gmall.cms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.cms.entity.Subject;
import com.atguigu.gmall.cms.entity.SubjectProductRelation;
import com.atguigu.gmall.cms.mapper.SubjectMapper;
import com.atguigu.gmall.cms.mapper.SubjectProductRelationMapper;
import com.atguigu.gmall.cms.service.SubjectService;
import com.atguigu.gmall.utils.PageMapUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 专题表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Component
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Autowired
    private SubjectProductRelationMapper subjectProductRelationMapper;

    @Override
    public List<Subject> getSubjectList() {
        List<Subject> subjectList = baseMapper.selectList(null);
        return subjectList;
    }

    @Override
    public Map<String, Object> getSubjectByKeyword(Integer pageNum, Integer pageSize, String keyword) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper.like("title", keyword);
        }
        Page<Subject> page = new Page<>(pageNum, pageSize);
        IPage<Subject> subjectIPage = baseMapper.selectPage(page, queryWrapper);
        Map<String, Object> result = PageMapUtils.getPageMap(subjectIPage);
        return result;
    }

    @Override
    public List<SubjectProductRelation> getSubjectProductRelationByProductId(Long productId) {
        QueryWrapper<SubjectProductRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        List<SubjectProductRelation> subjectProductRelationList = subjectProductRelationMapper.selectList(queryWrapper);
        return subjectProductRelationList;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void updateSubjectProductRelationList(List<SubjectProductRelation> subjectProductRelationList) {
        subjectProductRelationList.forEach(subjectProductRelation -> {
            subjectProductRelationMapper.updateById(subjectProductRelation);
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveSubjectProductRelationList(List<SubjectProductRelation> subjectProductRelationList,Long id) {
        subjectProductRelationList.forEach(subjectProductRelation -> {
            subjectProductRelation.setProductId(id);
            subjectProductRelationMapper.insert(subjectProductRelation);
        });
    }



}
