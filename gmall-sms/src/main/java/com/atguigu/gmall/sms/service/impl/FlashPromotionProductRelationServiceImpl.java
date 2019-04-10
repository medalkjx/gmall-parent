package com.atguigu.gmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.pms.entity.Product;
import com.atguigu.gmall.pms.service.ProductService;
import com.atguigu.gmall.sms.entity.FlashPromotionProductRelation;
import com.atguigu.gmall.sms.mapper.FlashPromotionProductRelationMapper;
import com.atguigu.gmall.sms.service.FlashPromotionProductRelationService;
import com.atguigu.gmall.sms.vo.FlashPromotionProductRelationParam;
import com.atguigu.gmall.utils.PageMapUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品限时购与商品关系表 服务实现类
 * </p>
 *
 * @author mei
 * @since 2019-03-25
 */
@Component
@Service
public class FlashPromotionProductRelationServiceImpl extends ServiceImpl<FlashPromotionProductRelationMapper, FlashPromotionProductRelation> implements FlashPromotionProductRelationService {

    @Reference
    ProductService productService;

    @Override
    public Map<String, Object> pageFlashPromotionProductRelation(Long flashPromotionId, Long flashPromotionSessionId, Integer pageNum, Integer pageSize) {
        List<FlashPromotionProductRelationParam> list = new ArrayList<>();
        Page<FlashPromotionProductRelation> page = new Page<FlashPromotionProductRelation>(pageNum, pageSize);
        Page<FlashPromotionProductRelationParam> paramPage = new Page<FlashPromotionProductRelationParam>();
        QueryWrapper<FlashPromotionProductRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("flash_promotion_id", flashPromotionId);
        queryWrapper.eq("flash_promotion_session_id", flashPromotionSessionId);
        IPage<FlashPromotionProductRelation> selectPage = baseMapper.selectPage(page, queryWrapper);
        BeanUtils.copyProperties(selectPage, paramPage);

        List<FlashPromotionProductRelation> flashPromotionProductRelations = selectPage.getRecords();
        flashPromotionProductRelations.forEach(flashPromotionProductRelation -> {
            FlashPromotionProductRelationParam flashPromotionProductRelationParam = new FlashPromotionProductRelationParam();
            BeanUtils.copyProperties(flashPromotionProductRelation, flashPromotionProductRelationParam);
            Long productId = flashPromotionProductRelation.getProductId();
            Product product = productService.getById(productId);
            flashPromotionProductRelationParam.setProduct(product);
            list.add(flashPromotionProductRelationParam);
        });
        paramPage.setRecords(list);
        Map<String, Object> pageMap = PageMapUtils.getPageMap(paramPage);
        return pageMap;
    }

    @Override
    public boolean create(List<FlashPromotionProductRelation> flashPromotionProductRelationList) {
        int count = 0;
        for (FlashPromotionProductRelation flashPromotionProductRelation : flashPromotionProductRelationList) {
            Integer insert = baseMapper.insert(flashPromotionProductRelation);
            if (null != insert && insert > 0) {
                count++;
            }
        }
        if (flashPromotionProductRelationList.size() == count) {
            return true;
        }
        return false;
    }
}
