package com.atguigu.gmall.sms.service;

import com.atguigu.gmall.sms.entity.HomeAdvertise;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 首页轮播广告表 服务类
 * </p>
 *
 * @author mei
 * @since 2019-03-25
 */
public interface HomeAdvertiseService extends IService<HomeAdvertise> {

    /**
     * 分页查询广告
     *
     * @param name
     * @param type
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @return
     */
    Map<String, Object> pageHomeAdvertise(String name, Integer type, String endTime, Integer pageNum, Integer pageSize);

    /**
     * 修改上下线状态
     *
     * @param status
     * @param id
     * @return
     */
    boolean updateStatus(Integer status, Long id);
}
