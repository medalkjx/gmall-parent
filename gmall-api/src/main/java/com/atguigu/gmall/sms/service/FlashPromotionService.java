package com.atguigu.gmall.sms.service;

import com.atguigu.gmall.sms.entity.FlashPromotion;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 限时购表 服务类
 * </p>
 *
 * @author mei
 * @since 2019-03-25
 */
public interface FlashPromotionService extends IService<FlashPromotion> {

    /**
     * 根据活动名称分页查询
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    Map<String,Object> pageFlashPromotion(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 修改上下线状态
     * @param id
     * @param status
     */
    void updateStatusById(Long id, Integer status);
}
