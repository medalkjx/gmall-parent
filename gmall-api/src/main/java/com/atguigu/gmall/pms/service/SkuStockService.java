package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.SkuStock;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
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
     *
     * @param pid
     * @param keyword
     * @return
     */
    List<SkuStock> getListByPidOrKeyword(Long pid, String keyword);

    /**
     * 批量更新库存信息
     *
     * @param pid
     * @param skuStockList
     * @return
     */
    boolean updateSkuStockByPid(Long pid, List<SkuStock> skuStockList);

    /**
     * 获取商品所有的Sku信息
     *
     * @param productId
     * @return
     */
    List<SkuStock> getAllSkuInfoByProductId(Long productId);

    /**
     * 根据商品的SkuId获取商品价格
     *
     * @param productSkuId
     * @return
     */
    BigDecimal getSkuPriceById(Long productSkuId);

    /**
     * 修改SkuStock lock_stock ,锁定库存
     *
     * @param skuId
     * @param quantity
     */
    void updateStock(Long skuId, Integer quantity);

    /**
     * 修改SkuStock lock_stock ,释放库存
     *
     * @param skuId
     * @param quantity
     */
    void releaseStock(Long skuId, Integer quantity);
}
