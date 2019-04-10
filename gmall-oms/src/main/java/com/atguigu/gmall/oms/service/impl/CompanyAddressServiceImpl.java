package com.atguigu.gmall.oms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.oms.entity.CompanyAddress;
import com.atguigu.gmall.oms.mapper.CompanyAddressMapper;
import com.atguigu.gmall.oms.service.CompanyAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 公司收发货地址表 服务实现类
 * </p>
 *
 * @author mei
 * @since 2019-04-03
 */
@Component
@Service
public class CompanyAddressServiceImpl extends ServiceImpl<CompanyAddressMapper, CompanyAddress> implements CompanyAddressService {

}
