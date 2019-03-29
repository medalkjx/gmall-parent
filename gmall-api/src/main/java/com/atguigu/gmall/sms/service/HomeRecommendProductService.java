package com.atguigu.gmall.sms.service;

import com.atguigu.gmall.sms.entity.HomeRecommendProduct;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 人气推荐商品表 服务类
 * </p>
 *
 * @author mei
 * @since 2019-03-25
 */
public interface HomeRecommendProductService extends IService<HomeRecommendProduct> {

    /**
     * 分页查询推荐
     *
     * @param productName
     * @param recommendStatus
     * @param pageNum
     * @param pageSize
     * @return
     */
    Map<String, Object> pageHomeRecommendProduct(String productName, Integer recommendStatus, Integer pageNum, Integer pageSize);

    /**
     * 添加首页推荐
     *
     * @param homeRecommendProductList
     * @return
     */
    boolean create(List<HomeRecommendProduct> homeRecommendProductList);

    /**
     * 批量删除推荐
     *
     * @param ids
     * @return
     */
    boolean delete(List<Long> ids);

    /**
     * 批量修改推荐状态
     *
     * @param recommendStatus
     * @param ids
     * @return
     */
    boolean updateRecommendStatus(Integer recommendStatus, List<Long> ids);

    /**
     * 修改推荐排序
     *
     * @param id
     * @param sort
     * @return
     */
    boolean updateSort(Long id, Integer sort);
}
