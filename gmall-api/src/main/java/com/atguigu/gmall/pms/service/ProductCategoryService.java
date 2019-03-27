package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.ProductCategory;
import com.atguigu.gmall.pms.vo.PmsProductCategoryParam;
import com.atguigu.gmall.pms.vo.PmsProductCategoryWithChildrenItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品分类 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface ProductCategoryService extends IService<ProductCategory> {

    /**
     * 分页查询商品分类
     * @param pageNum
     * @param pageSize
     * @param parentId
     * @return
     */
    Map<String,Object> pageProductCategory(Integer pageNum, Integer pageSize, Long parentId);

    /**
     * 添加产品分类
     * @param productCategoryParam
     * @return
     */
    boolean createProductCategory(PmsProductCategoryParam productCategoryParam);

    /**
     * 修改商品分类
     * @param id
     * @param productCategoryParam
     * @return
     */
    boolean updateProductCategoryById(Long id, PmsProductCategoryParam productCategoryParam);

    /**
     * 修改导航栏显示状态
     * @param ids
     * @param navStatus
     * @return
     */
    boolean updateNavStatusByIds(List<Long> ids, Integer navStatus);

    /**
     *
     * @param ids
     * @param showStatus
     * @return
     */
    boolean updateShowStatusByIds(List<Long> ids, Integer showStatus);

    /**
     * 查询所有一级分类及子分类
     * @return
     */
    List<PmsProductCategoryWithChildrenItem> listWithChildren();

    /**
     * 删除商品分类
     * @param id
     * @return
     */
    boolean deleteProductCategoryById(Long id);
}
