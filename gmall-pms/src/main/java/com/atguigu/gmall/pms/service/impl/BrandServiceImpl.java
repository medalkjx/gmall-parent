package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.pms.entity.Brand;
import com.atguigu.gmall.pms.mapper.BrandMapper;
import com.atguigu.gmall.pms.service.BrandService;
import com.atguigu.gmall.pms.vo.PmsBrandParam;
import com.atguigu.gmall.utils.PageMapUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 品牌表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Service
@Component
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

    @Override
    public Map<String, Object> pageBrand(Integer pageNum, Integer pageSize, String keyword) {
        BrandMapper brandMapper = getBaseMapper();
        Page<Brand> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Brand> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper.like("name", keyword).or()
                    .eq("first_letter", keyword);
        }
        IPage<Brand> pageInfo = brandMapper.selectPage(page, queryWrapper);
        Map<String, Object> result = PageMapUtils.getPageMap(pageInfo);
        return result;
    }

    @Override
    public List<Brand> listAll() {
        return baseMapper.selectList(null);
    }

    @Override
    public Brand getBrandById(Long id) {
        return baseMapper.selectById(id);
    }

    @Transactional
    @Override
    public boolean createBrand(PmsBrandParam pmsBrand) {
        Brand brand = new Brand();
        BeanUtils.copyProperties(pmsBrand, brand);
        Integer result = baseMapper.insert(brand);
        return result != null && result > 0;
    }

    @Transactional
    @Override
    public boolean updateById(Long id, PmsBrandParam pmsBrandParam) {
        Brand brand = new Brand();
        BeanUtils.copyProperties(pmsBrandParam, brand);
        brand.setId(id);
        Integer result = baseMapper.updateById(brand);
        return null != result && result > 0;
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {
        Integer result = baseMapper.deleteById(id);
        return null != result && result > 0;
    }

    @Transactional
    @Override
    public boolean deleteByIds(List<Long> ids) {
        Integer result = baseMapper.deleteBatchIds(ids);
        return null != result && result > 0;
    }

    @Transactional
    @Override
    public boolean updateShowStatusByIds(List<Long> ids, Integer showStatus) {
        Brand brand = new Brand();
        brand.setShowStatus(showStatus);
        QueryWrapper<Brand> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",ids);
        Integer result = baseMapper.update(brand, queryWrapper);
        return null != result && result > 0;
    }

    @Transactional
    @Override
    public boolean updateFactoryStatusByIds(List<Long> ids, Integer factoryStatus) {
        Brand brand = new Brand();
        brand.setFactoryStatus(factoryStatus);
        QueryWrapper<Brand> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",ids);
        Integer result = baseMapper.update(brand, queryWrapper);
        return null != result && result > 0;
    }
}
