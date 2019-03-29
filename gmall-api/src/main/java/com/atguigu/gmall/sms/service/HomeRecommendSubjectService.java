package com.atguigu.gmall.sms.service;

import com.atguigu.gmall.sms.entity.HomeRecommendSubject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 首页推荐专题表 服务类
 * </p>
 *
 * @author mei
 * @since 2019-03-25
 */
public interface HomeRecommendSubjectService extends IService<HomeRecommendSubject> {

    /**
     * 分页查询推荐
     *
     * @param subjectName
     * @param recommendStatus
     * @param pageNum
     * @param pageSize
     * @return
     */
    Map<String, Object> pageHomeRecommendSubject(String subjectName, Integer recommendStatus, Integer pageNum, Integer pageSize);

    /**
     * 添加首页推荐专题
     *
     * @param homeRecommendSubjectList
     * @return
     */
    boolean create(List<HomeRecommendSubject> homeRecommendSubjectList);

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
     * @param ids
     * @param recommendStatus
     * @return
     */
    boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 修改推荐排序
     *
     * @param id
     * @param sort
     * @return
     */
    boolean updateSort(Long id, Integer sort);
}
