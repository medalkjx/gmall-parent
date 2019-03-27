package com.atguigu.gmall.sms.service;

import com.atguigu.gmall.sms.entity.FlashPromotionSession;
import com.atguigu.gmall.sms.vo.FlashPromotionSessionCountResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 限时购场次表 服务类
 * </p>
 *
 * @author mei
 * @since 2019-03-25
 */
public interface FlashPromotionSessionService extends IService<FlashPromotionSession> {

    /**
     * 获取全部场次
     *
     * @return
     */
    List<FlashPromotionSession> selectList();

    /**
     * 修改启用状态
     *
     * @param id
     * @param status
     */
    void updateStatus(Long id, Integer status);

    /**
     * 获取全部可选场次及其数量
     * @param flashPromotionId
     * @return
     */
    List<FlashPromotionSessionCountResult> getFlashPromotionSessionCount(Long flashPromotionId);
}
