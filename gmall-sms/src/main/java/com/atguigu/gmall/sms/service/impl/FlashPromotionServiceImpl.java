package com.atguigu.gmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.sms.entity.FlashPromotion;
import com.atguigu.gmall.sms.mapper.FlashPromotionMapper;
import com.atguigu.gmall.sms.service.FlashPromotionService;
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
 * 限时购表 服务实现类
 * </p>
 *
 * @author mei
 * @since 2019-03-25
 */
@Component
@Service
public class FlashPromotionServiceImpl extends ServiceImpl<FlashPromotionMapper, FlashPromotion> implements FlashPromotionService {

    @Override
    public Map<String, Object> pageFlashPromotion(String keyword, Integer pageNum, Integer pageSize) {
        Page<FlashPromotion> page = new Page<>(pageNum,pageSize);
        QueryWrapper<FlashPromotion> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(keyword)){
            queryWrapper.like("title",keyword);
        }
        IPage<FlashPromotion> pageInfo = baseMapper.selectPage(page, queryWrapper);
        Map<String, Object> map = PageMapUtils.getPageMap(pageInfo);
        return map;
    }

    @Override
    public void updateStatusById(Long id, Integer status) {
        FlashPromotion flashPromotion = new FlashPromotion();
        flashPromotion.setId(id);
        flashPromotion.setStatus(status);
        baseMapper.updateById(flashPromotion);
    }
}
