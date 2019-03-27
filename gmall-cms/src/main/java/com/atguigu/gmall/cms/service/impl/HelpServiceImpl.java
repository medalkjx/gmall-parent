package com.atguigu.gmall.cms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.cms.entity.Help;
import com.atguigu.gmall.cms.mapper.HelpMapper;
import com.atguigu.gmall.cms.service.HelpService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 帮助表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Component
@Service
public class HelpServiceImpl extends ServiceImpl<HelpMapper, Help> implements HelpService {

}
