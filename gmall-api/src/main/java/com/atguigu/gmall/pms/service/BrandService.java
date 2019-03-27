package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.Brand;
import com.atguigu.gmall.pms.vo.PmsBrandParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 品牌表 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface BrandService extends IService<Brand> {

    /**
     * 根据品牌名称分页获取品牌列表
     * @param pageNum
     * @param pageSize
     * @param keyword
     * @return
     */
    Map<String,Object> pageBrand(Integer pageNum, Integer pageSize, String keyword);

    /**
     * 获取全部品牌列表
     * @return
     */
    List<Brand> listAll();

    /**
     * 根据编号查询品牌信息
     * @param id
     * @return
     */
    Brand getBrandById(Long id);

    /**
     * 添加品牌
     * @param pmsBrand
     * @return
     */
    boolean createBrand(PmsBrandParam pmsBrand);

    /**
     * 根据id更新品牌
     * @param id
     * @param pmsBrandParam
     * @return
     */
    boolean updateById(Long id, PmsBrandParam pmsBrandParam);

    /**
     * 删除品牌
     * @param id
     * @return
     */
    boolean deleteById(Long id);

    /**
     * 批量删除品牌
     * @param ids
     * @return
     */
    boolean deleteByIds(List<Long> ids);

    /**
     * 批量更新显示状态
     * @param ids
     * @param showStatus
     * @return
     */
    boolean updateShowStatusByIds(List<Long> ids, Integer showStatus);

    /**
     * 批量更新厂家制造商状态
     * @param ids
     * @param factoryStatus
     * @return
     */
    boolean updateFactoryStatusByIds(List<Long> ids, Integer factoryStatus);
}
