package com.atguigu.gmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.sms.entity.FlashPromotionProductRelation;
import com.atguigu.gmall.sms.entity.FlashPromotionSession;
import com.atguigu.gmall.sms.mapper.FlashPromotionProductRelationMapper;
import com.atguigu.gmall.sms.mapper.FlashPromotionSessionMapper;
import com.atguigu.gmall.sms.service.FlashPromotionSessionService;
import com.atguigu.gmall.sms.vo.FlashPromotionSessionCountResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 限时购场次表 服务实现类
 * </p>
 *
 * @author mei
 * @since 2019-03-25
 */
@Component
@Service
public class FlashPromotionSessionServiceImpl extends ServiceImpl<FlashPromotionSessionMapper, FlashPromotionSession> implements FlashPromotionSessionService {

    @Autowired
    FlashPromotionProductRelationMapper flashPromotionProductRelationMapper;

    @Override
    public List<FlashPromotionSession> selectList() {
        List<FlashPromotionSession> promotionSessionList = baseMapper.selectList(null);
        return promotionSessionList;
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        FlashPromotionSession flashPromotionSession = new FlashPromotionSession();
        flashPromotionSession.setId(id);
        flashPromotionSession.setStatus(status);
        baseMapper.updateById(flashPromotionSession);
    }

    @Override
    public List<FlashPromotionSessionCountResult> getFlashPromotionSessionCount(Long flashPromotionId) {
        //todo 获取全部可选场次及其数量
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<FlashPromotionSessionCountResult> sessionCountResults = new ArrayList<>();
        List<FlashPromotionSession> flashPromotionSessionList = baseMapper.selectList(new QueryWrapper<FlashPromotionSession>().eq("status", "1"));
        flashPromotionSessionList.forEach(flashPromotionSession -> {
            FlashPromotionSessionCountResult flashPromotionSessionCountResult = new FlashPromotionSessionCountResult();
            BeanUtils.copyProperties(flashPromotionSession, flashPromotionSessionCountResult);
            Long flashPromotionSessionId = flashPromotionSession.getId();
            Integer count = flashPromotionProductRelationMapper.selectCount(
                    new QueryWrapper<FlashPromotionProductRelation>().
                            eq("flash_promotion_id", flashPromotionId).
                            eq("flash_promotion_session_id", flashPromotionSessionId));
            flashPromotionSessionCountResult.setProductCount(count);
            sessionCountResults.add(flashPromotionSessionCountResult);
        });
        return sessionCountResults;
    }
}
