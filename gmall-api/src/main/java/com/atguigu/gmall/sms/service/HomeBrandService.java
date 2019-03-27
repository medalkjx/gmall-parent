package com.atguigu.gmall.sms.service;

import com.atguigu.gmall.sms.entity.HomeBrand;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 首页推荐品牌表 服务类
 * </p>
 *
 * @author mei
 * @since 2019-03-25
 */
public interface HomeBrandService extends IService<HomeBrand> {

    /**
     * 分页查询推荐品牌
     *
     * @param brandName
     * @param recommendStatus
     * @param pageNum
     * @param pageSize
     * @return
     */
    Map<String, Object> pageHomeBrand(String brandName, Integer recommendStatus, Integer pageNum, Integer pageSize);

    /**
     * 添加首页推荐品牌
     *
     * @param homeBrandList
     * @return
     */
    boolean createHomeBrands(List<HomeBrand> homeBrandList);

    /**
     * 批量删除推荐商品
     *
     * @param ids
     * @return
     */
    boolean deleteByIds(List<Long> ids);

    /**
     * 修改品牌排序
     *
     * @param id
     * @param sort
     * @return
     */
    boolean updateSortById(Long id, Integer sort);

    /**
     * 批量修改推荐状态
     *
     * @param ids
     * @param recommendStatus
     * @return
     */
    boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus);
}
