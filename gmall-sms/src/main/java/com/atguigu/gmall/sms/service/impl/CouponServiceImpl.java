package com.atguigu.gmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.sms.entity.Coupon;
import com.atguigu.gmall.sms.entity.CouponProductCategoryRelation;
import com.atguigu.gmall.sms.entity.CouponProductRelation;
import com.atguigu.gmall.sms.mapper.CouponMapper;
import com.atguigu.gmall.sms.mapper.CouponProductCategoryRelationMapper;
import com.atguigu.gmall.sms.mapper.CouponProductRelationMapper;
import com.atguigu.gmall.sms.service.CouponService;
import com.atguigu.gmall.sms.vo.CouponRelationParam;
import com.atguigu.gmall.utils.PageMapUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 优惠卷表 服务实现类
 * </p>
 *
 * @author mei
 * @since 2019-03-25
 */
@Service
@Component
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

    @Autowired
    CouponProductCategoryRelationMapper couponProductCategoryRelationMapper;

    @Autowired
    CouponProductRelationMapper couponProductRelationMapper;

    ThreadLocal<Coupon> threadLocal = new ThreadLocal<>();

    @Override
    public Map<String, Object> pageCoupon(String name, Integer type, Integer pageNum, Integer pageSize) {
        Page<Coupon> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Coupon> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(type)) {
            queryWrapper.eq("type", type);
        }
        IPage<Coupon> pageInfo = baseMapper.selectPage(page, queryWrapper);
        Map<String, Object> pageMap = PageMapUtils.getPageMap(pageInfo);
        return pageMap;
    }

    @Override
    public CouponRelationParam getCouponRelationParam(Long id) {
        CouponRelationParam couponRelationParam = new CouponRelationParam();
        Coupon coupon = baseMapper.selectById(id);
        BeanUtils.copyProperties(coupon, couponRelationParam);
        List<CouponProductCategoryRelation> productCategoryRelationList = couponProductCategoryRelationMapper.selectCouponProductCategoryRelationByCouponId(id);
        couponRelationParam.setCouponProductCategoryRelationList(productCategoryRelationList);
        List<CouponProductRelation> productRelationList = couponProductRelationMapper.selectCouponProductCategoryRelationByCouponId(id);
        couponRelationParam.setCouponProductRelationList(productRelationList);
        return couponRelationParam;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void createCouponRelationParam(CouponRelationParam couponRelationParam) {
        CouponServiceImpl psProxy = (CouponServiceImpl) AopContext.currentProxy();
        psProxy.saveCoupon(couponRelationParam);
        psProxy.saveCouponProductRelationList(couponRelationParam.getCouponProductRelationList());
        psProxy.saveCouponProductCategoryRelationList(couponRelationParam.getCouponProductCategoryRelationList());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteById(Long id) {
        baseMapper.deleteById(id);
        couponProductRelationMapper.deleteByCouponId(id);
        couponProductCategoryRelationMapper.deleteByCouponId(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateCouponInfoById(CouponRelationParam couponRelationParam, Long id) {
        CouponServiceImpl proxy = (CouponServiceImpl) AopContext.currentProxy();
        proxy.updateCoupon(couponRelationParam, id);
        proxy.updatecouponProductRelation(couponRelationParam.getCouponProductRelationList());
        proxy.updateCouponProductCategoryRelation(couponRelationParam.getCouponProductCategoryRelationList());
    }

    @Override
    public Coupon getCouponByProductId(Long id) {
        return baseMapper.selectCouponByProductId(id);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateCouponProductCategoryRelation(List<CouponProductCategoryRelation> couponProductCategoryRelationList) {
        Coupon coupon = threadLocal.get();
        couponProductCategoryRelationList.forEach(couponProductCategoryRelation -> {
            couponProductCategoryRelation.setCouponId(coupon.getId());
            couponProductCategoryRelationMapper.updateById(couponProductCategoryRelation);
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updatecouponProductRelation(List<CouponProductRelation> couponProductRelationList) {
        Coupon coupon = threadLocal.get();
        couponProductRelationList.forEach(couponProductRelation -> {
            couponProductRelation.setProductId(coupon.getId());
            couponProductRelationMapper.updateById(couponProductRelation);
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateCoupon(CouponRelationParam couponRelationParam, Long id) {
        Coupon coupon = new Coupon();
        BeanUtils.copyProperties(couponRelationParam, coupon);
        coupon.setId(id);
        baseMapper.updateById(coupon);
        threadLocal.set(coupon);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveCoupon(CouponRelationParam couponRelationParam) {
        Coupon coupon = new Coupon();
        BeanUtils.copyProperties(couponRelationParam, coupon);
        baseMapper.insert(coupon);
        threadLocal.set(coupon);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveCouponProductCategoryRelationList(List<CouponProductCategoryRelation> couponProductCategoryRelationList) {
        Coupon coupon = threadLocal.get();
        couponProductCategoryRelationList.forEach(couponProductCategoryRelation -> {
            couponProductCategoryRelation.setCouponId(coupon.getId());
            couponProductCategoryRelationMapper.insert(couponProductCategoryRelation);
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveCouponProductRelationList(List<CouponProductRelation> couponProductRelationList) {
        Coupon coupon = threadLocal.get();
        couponProductRelationList.forEach(couponProductRelation -> {
            couponProductRelation.setCouponId(coupon.getId());
            couponProductRelationMapper.insert(couponProductRelation);
        });
    }

}
