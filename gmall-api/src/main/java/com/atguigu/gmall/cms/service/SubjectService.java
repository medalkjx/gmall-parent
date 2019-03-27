package com.atguigu.gmall.cms.service;

import com.atguigu.gmall.cms.entity.Subject;
import com.atguigu.gmall.cms.entity.SubjectProductRelation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 专题表 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface SubjectService extends IService<Subject> {

    /**
     * 获取全部商品专题
     * @return
     */
    List<Subject> getSubjectList();

    /**
     * 根据专题名称分页获取专题
     * @param pageNum
     * @param pageSize
     * @param keyword
     * @return
     */
    Map<String,Object> getSubjectByKeyword(Integer pageNum, Integer pageSize, String keyword);

    /**
     * 根据商品id获取专题和商品关系
     * @param productId
     * @return
     */
    List<SubjectProductRelation> getSubjectProductRelationByProductId(Long productId);

    /**
     * 更新专题和商品关系
     * @param subjectProductRelationList
     * @return
     */
    void updateSubjectProductRelationList(List<SubjectProductRelation> subjectProductRelationList);
    /**
     * 批量插入商品专题
     * @param subjectProductRelationList
     * @param id
     */
    void saveSubjectProductRelationList(List<SubjectProductRelation> subjectProductRelationList, Long id);

}
