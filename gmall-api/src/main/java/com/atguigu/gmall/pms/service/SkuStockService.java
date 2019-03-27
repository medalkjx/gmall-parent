package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.SkuStock;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * sku的库存 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface SkuStockService extends IService<SkuStock> {

    /**
     * 根据商品编号及编号模糊搜索sku库存
     * @param pid
     * @param keyword
     * @return
     */
    List<SkuStock> getListByPidOrKeyword(Long pid, String keyword);

    /**
     * 批量更新库存信息
     * @param pid
     * @param skuStockList
     * @return
     */
    boolean updateSkuStockByPid(Long pid, List<SkuStock> skuStockList);
}
