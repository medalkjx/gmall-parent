package com.atguigu.gmall.cms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.cms.entity.PrefrenceArea;
import com.atguigu.gmall.cms.entity.PrefrenceAreaProductRelation;
import com.atguigu.gmall.cms.mapper.PrefrenceAreaMapper;
import com.atguigu.gmall.cms.mapper.PrefrenceAreaProductRelationMapper;
import com.atguigu.gmall.cms.service.PrefrenceAreaService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 优选专区 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Component
@Service
public class PrefrenceAreaServiceImpl extends ServiceImpl<PrefrenceAreaMapper, PrefrenceArea> implements PrefrenceAreaService {

    @Autowired
    private PrefrenceAreaProductRelationMapper prefrenceAreaProductRelationMapper;

    @Override
    public List<PrefrenceArea> getPrefrenceAreaList() {
        return baseMapper.selectList(null);
    }

    @Override
    public List<PrefrenceAreaProductRelation> getPrefrenceAreaProductRelationListByProductId(Long productId) {
        QueryWrapper<PrefrenceAreaProductRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        List<PrefrenceAreaProductRelation> prefrenceAreaProductRelationList = prefrenceAreaProductRelationMapper.selectList(queryWrapper);
        return prefrenceAreaProductRelationList;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void updatePreFrenceAreaProductRelationList(List<PrefrenceAreaProductRelation> prefrenceAreaProductRelationList) {
        prefrenceAreaProductRelationList.forEach(prefrenceAreaProductRelation -> {
            prefrenceAreaProductRelationMapper.updateById(prefrenceAreaProductRelation);
        });
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void savePrefrenceAreaProductRelationList(List<PrefrenceAreaProductRelation> prefrenceAreaProductRelationList, Long id) {
        prefrenceAreaProductRelationList.forEach(prefrenceAreaProductRelation -> {
            prefrenceAreaProductRelation.setProductId(id);
            prefrenceAreaProductRelationMapper.insert(prefrenceAreaProductRelation);
        });
    }

}
