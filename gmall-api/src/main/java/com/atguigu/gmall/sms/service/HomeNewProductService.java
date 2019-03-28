package com.atguigu.gmall.sms.service;

import com.atguigu.gmall.sms.entity.HomeNewProduct;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 新鲜好物表 服务类
 * </p>
 *
 * @author mei
 * @since 2019-03-25
 */
public interface HomeNewProductService extends IService<HomeNewProduct> {

    /**
     * 分页查询推荐
     *
     * @param productName
     * @param recommendStatus
     * @param pageNum
     * @param pageSize
     * @return
     */
    Map<String, Object> pageHomeNewProduct(String productName, Integer recommendStatus, Integer pageNum, Integer pageSize);

    /**
     * 添加推荐
     *
     * @param list
     * @return
     */
    boolean create(List<HomeNewProduct> list);

    /**
     * 批量删除推荐
     *
     * @param ids
     * @return
     */
    boolean deleteByIds(List<Long> ids);

    /**
     * 批量修改推荐状态
     *
     * @param ids
     * @param recommendStatus
     * @return
     */
    boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 修改推荐排序
     *
     * @param sort
     * @param id
     * @return
     */
    boolean updateSortById(Integer sort, Long id);
}
