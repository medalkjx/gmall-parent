package com.atguigu.gmall.cms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.cms.entity.SubjectComment;
import com.atguigu.gmall.cms.mapper.SubjectCommentMapper;
import com.atguigu.gmall.cms.service.SubjectCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 专题评论表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Component
@Service
public class SubjectCommentServiceImpl extends ServiceImpl<SubjectCommentMapper, SubjectComment> implements SubjectCommentService {

}
