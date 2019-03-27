package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.pms.entity.SkuStock;
import com.atguigu.gmall.pms.mapper.SkuStockMapper;
import com.atguigu.gmall.pms.service.SkuStockService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * sku的库存 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Service
@Component
public class SkuStockServiceImpl extends ServiceImpl<SkuStockMapper, SkuStock> implements SkuStockService {

    @Override
    public List<SkuStock> getListByPidOrKeyword(Long pid, String keyword) {
        QueryWrapper<SkuStock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id",pid);
        if (!StringUtils.isEmpty(keyword)){
            queryWrapper.like("sku_code",keyword);
        }
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public boolean updateSkuStockByPid(Long pid, List<SkuStock> skuStockList) {
        for (SkuStock skuStock : skuStockList) {
            QueryWrapper<SkuStock> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",skuStock.getId())
                    .eq("product_id",skuStock.getProductId());
            Integer result = baseMapper.update(skuStock, queryWrapper);
            if(!(null !=result && result >0)){
                return false;
            }
        }
        return true;
    }
}
