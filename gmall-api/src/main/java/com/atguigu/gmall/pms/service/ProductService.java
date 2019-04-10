package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.Product;
import com.atguigu.gmall.pms.vo.PmsProductParam;
import com.atguigu.gmall.pms.vo.PmsProductQueryParam;
import com.atguigu.gmall.search.vo.EsProductAttributeValue;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品信息 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface ProductService extends IService<Product> {

    /**
     * 获取商品分页数据
     *
     * @param pageSize
     * @param pageNum
     * @return
     */
    Map<String, Object> pageProduct(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum);

    /**
     * 根据商品名称或货号模糊查询
     *
     * @param keyword
     * @return
     */
    List<Product> getProductListByKeyword(String keyword);

    /**
     * 批量上下架
     *
     * @param ids
     * @param publishStatus
     * @return
     */
    void updatePublishStatusByIds(List<Long> ids, Integer publishStatus);

    /**
     * 批量推荐商品
     *
     * @param ids
     * @param recommendStatus
     * @return
     */
    boolean updateRecommendStatusByIds(List<Long> ids, Integer recommendStatus);

    /**
     * 批量设为新品
     *
     * @param ids
     * @param newStatus
     * @return
     */
    boolean updateNewStatusByIds(List<Long> ids, Integer newStatus);

    /**
     * 根据商品id获取商品编辑信息
     *
     * @param ids
     * @param deleteStatus
     * @return
     */
    boolean updateDeleteStatusByIds(List<Long> ids, Integer deleteStatus);

    /**
     * 批量修改审核状态
     *
     * @param ids
     * @param verifyStatus
     * @param detail
     * @return
     */
    boolean updateVerifyStatusByIds(List<Long> ids, Integer verifyStatus, String detail);

    /**
     * 更新商品
     * @param id
     * @param productParam
     * @return
     */

    /**
     * 根据商品id获取商品编辑信息
     *
     * @param id
     * @return
     */
    PmsProductParam getProductInfoById(Long id);

    /**
     * 创建商品
     *
     * @param productParam
     * @return
     */
    void createProductParam(@Valid PmsProductParam productParam);

    /**
     * 修改商品
     *
     * @param id
     * @param productParam
     */
    void updateProductParamById(Long id, @Valid PmsProductParam productParam);

    /**
     * 获取缓存中商品信息
     *
     * @param productId
     * @return
     */
    Product getProductByIdFromCache(Long productId);

    /**
     * 获取商品的销售属性
     *
     * @param productId
     * @return
     */
    List<EsProductAttributeValue> getProductSaleAttr(Long productId);

    /**
     * 获取商品基本属性
     *
     * @param productId
     * @return
     */
    List<EsProductAttributeValue> getProductBaseAttr(Long productId);
}
