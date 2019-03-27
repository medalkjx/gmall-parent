package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.cms.entity.PrefrenceAreaProductRelation;
import com.atguigu.gmall.cms.entity.SubjectProductRelation;
import com.atguigu.gmall.cms.service.PrefrenceAreaService;
import com.atguigu.gmall.cms.service.SubjectService;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.mapper.*;
import com.atguigu.gmall.pms.service.ProductService;
import com.atguigu.gmall.pms.vo.PmsProductParam;
import com.atguigu.gmall.pms.vo.PmsProductQueryParam;
import com.atguigu.gmall.search.service.GmallSearchService;
import com.atguigu.gmall.search.vo.EsProduct;
import com.atguigu.gmall.search.vo.EsProductAttributeValue;
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

import javax.validation.Valid;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */

@Component
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductLadderMapper productLadderMapper;
    @Autowired
    private ProductFullReductionMapper productFullReductionMapper;
    @Autowired
    private MemberPriceMapper memberPriceMapper;
    @Autowired
    private SkuStockMapper skuStockMapper;
    @Autowired
    ProductCategoryMapper productCategoryMapper;
    @Autowired
    private ProductAttributeValueMapper productAttributeValueMapper;
    @Reference
    SubjectService subjectService;
    @Reference
    PrefrenceAreaService prefrenceAreaService;
    @Reference
    GmallSearchService searchService;

    ThreadLocal<Product> threadLocal = new ThreadLocal<>();

    @Override
    public Map<String, Object> pageProduct(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum) {
        ProductMapper productMapper = getBaseMapper();
        Page<Product> productPage = new Page<>(pageNum, pageSize);
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        if (productQueryParam != null) {
            if (!StringUtils.isEmpty(productQueryParam.getKeyword())) {
                queryWrapper.like("name", productQueryParam.getKeyword());
            }
            if (!StringUtils.isEmpty(productQueryParam.getProductSn())) {
                queryWrapper.eq("product_sn", productQueryParam.getProductSn());
            }
            if (!StringUtils.isEmpty(productQueryParam.getProductCategoryId())) {
                queryWrapper.eq("product_attribute_category_id", productQueryParam.getProductCategoryId());
            }
            if (!StringUtils.isEmpty(productQueryParam.getBrandId())) {
                queryWrapper.eq("brand_id", productQueryParam.getBrandId());
            }
            if (!StringUtils.isEmpty(productQueryParam.getPublishStatus())) {
                queryWrapper.eq("publish_status", productQueryParam.getPublishStatus());
            }
            if (!StringUtils.isEmpty(productQueryParam.getVerifyStatus())) {
                queryWrapper.eq("verify_status", productQueryParam.getVerifyStatus());
            }
        }

        IPage<Product> pageInfo = productMapper.selectPage(productPage, queryWrapper);
        Map<String, Object> result = PageMapUtils.getPageMap(pageInfo);
        return result;
    }

    @Override
    public List<Product> getProductListByKeyword(String keyword) {
        ProductMapper productMapper = getBaseMapper();
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_sn", keyword);
        queryWrapper.or().like("name", keyword);
        List<Product> products = productMapper.selectList(queryWrapper);
        return products;
    }

    @Override
    public void updatePublishStatusByIds(List<Long> ids, Integer publishStatus) {
        //Product product = new Product();
        //product.setPublishStatus(publishStatus);
        //QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        //queryWrapper.in("id", ids);
        //Integer result = baseMapper.update(product, queryWrapper);
        //return null != result && result > 0;
        if (publishStatus == 1) {
            publishProduct(ids);
        } else {
            removeProduct(ids);
        }
    }

    private void removeProduct(List<Long> ids) {
        //todo 下架
    }

    private void publishProduct(List<Long> ids) {
        ArrayList<Long> arrayList = new ArrayList<>();
        ids.forEach(id -> {
            Product product = baseMapper.selectById(id);
            List<SkuStock> skuStocks = skuStockMapper.selectList(new QueryWrapper<SkuStock>().eq("product_id", id));
            List<EsProductAttributeValue> attributeValues = productAttributeValueMapper.selectProductAttrValues(id);
            AtomicReference<Integer> count = new AtomicReference<>(0);
            skuStocks.forEach(skuStock -> {
                EsProduct esProduct = new EsProduct();
                BeanUtils.copyProperties(product, esProduct);
                if (skuStock.getSp1() == null) {
                    skuStock.setSp1(" ");
                }
                if (skuStock.getSp2() == null) {
                    skuStock.setSp2(" ");
                }
                if (skuStock.getSp3() == null) {
                    skuStock.setSp3(" ");
                }
                esProduct.setName(product.getName() +skuStock.getSp1()+ skuStock.getSp2() + skuStock.getSp3());
                esProduct.setPrice(skuStock.getPrice());
                esProduct.setStock(skuStock.getStock());
                esProduct.setSale(skuStock.getSale());
                esProduct.setAttrValueList(attributeValues);
                esProduct.setId(skuStock.getId());
                boolean es = searchService.saveProductInfoToES(esProduct);
                count.set(count.get()+1);
                if (es){
                    //todo 保存当前的id
                    arrayList.add(id);
                }
            });
            if (count.get()==skuStocks.size()){
                Product update = new Product();
                update.setId(product.getId());
                update.setPublishStatus(1);
                baseMapper.updateById(update);
            }else {
                //todo es arrayList.forEach(remove());
            }
        });
    }

    @Override
    public boolean updateRecommendStatusByIds(List<Long> ids, Integer recommendStatus) {
        Product product = new Product();
        product.setRecommandStatus(recommendStatus);
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        Integer result = baseMapper.update(product, queryWrapper);
        return null != result && result > 0;
    }

    @Override
    public boolean updateNewStatusByIds(List<Long> ids, Integer newStatus) {
        Product product = new Product();
        product.setNewStatus(newStatus);
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        Integer result = baseMapper.update(product, queryWrapper);
        return null != result && result > 0;
    }

    @Override
    public boolean updateDeleteStatusByIds(List<Long> ids, Integer deleteStatus) {
        Product product = new Product();
        product.setDeleteStatus(deleteStatus);
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        Integer result = baseMapper.update(product, queryWrapper);
        return null != result && result > 0;
    }

    @Override
    public boolean updateVerifyStatusByIds(List<Long> ids, Integer verifyStatus, String detail) {
        Product product = new Product();
        product.setVerifyStatus(verifyStatus);
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        Integer result = baseMapper.update(product, queryWrapper);
        return null != result && result > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateProductParamById(Long id, @Valid PmsProductParam productParam) {
        productParam.setId(id);
        ProductServiceImpl psProxy = (ProductServiceImpl) AopContext.currentProxy();
        psProxy.updateBaseInfo(productParam);
        psProxy.updateProductLadderList(productParam.getProductLadderList());
        psProxy.updateProductFullReductionList(productParam.getProductFullReductionList());
        psProxy.updateProductAttributeValueList(productParam.getProductAttributeValueList());
        psProxy.updateMemberPriceList(productParam.getMemberPriceList());
        prefrenceAreaService.updatePreFrenceAreaProductRelationList(productParam.getPrefrenceAreaProductRelationList());
        subjectService.updateSubjectProductRelationList(productParam.getSubjectProductRelationList());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateMemberPriceList(List<MemberPrice> memberPriceList) {
        for (MemberPrice memberPrice : memberPriceList) {
            Integer update = memberPriceMapper.updateById(memberPrice);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateProductAttributeValueList(List<ProductAttributeValue> productAttributeValueList) {
        for (ProductAttributeValue productAttributeValue : productAttributeValueList) {
            Integer update = productAttributeValueMapper.updateById(productAttributeValue);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateProductFullReductionList(List<ProductFullReduction> productFullReductionList) {
        for (ProductFullReduction productFullReduction : productFullReductionList) {
            Integer update = productFullReductionMapper.updateById(productFullReduction);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateBaseInfo(PmsProductParam productParam) {
        ProductServiceImpl psProxy = (ProductServiceImpl) AopContext.currentProxy();
        psProxy.updateProductInfo(productParam);
        psProxy.updateSkuStockList(productParam.getSkuStockList());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateProductInfo(PmsProductParam productParam) {
        Product product = new Product();
        BeanUtils.copyProperties(productParam, product);
        baseMapper.updateById(product);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSkuStockList(List<SkuStock> skuStockList) {
        for (SkuStock skuStock : skuStockList) {
            Integer update = skuStockMapper.updateById(skuStock);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateProductLadderList(List<ProductLadder> productLadderList) {
        for (ProductLadder productLadder : productLadderList) {
            Integer update = productLadderMapper.updateById(productLadder);
        }
    }

    /**
     * @param id
     * @return
     */
    @Override
    public PmsProductParam getProductInfoById(Long id) {
        PmsProductParam productParam = new PmsProductParam();
        Product product = baseMapper.selectById(id);
        BeanUtils.copyProperties(product, productParam);
        Long productId = product.getId();

        QueryWrapper<ProductLadder> ladderQueryWrapper = new QueryWrapper<>();
        ladderQueryWrapper.eq("product_id", productId);
        List<ProductLadder> productLadderList = productLadderMapper.selectList(ladderQueryWrapper);
        productParam.setProductLadderList(productLadderList);

        QueryWrapper<ProductFullReduction> fullReductionQueryWrapper = new QueryWrapper<>();
        fullReductionQueryWrapper.eq("product_id", productId);
        List<ProductFullReduction> productFullReductionList = productFullReductionMapper.selectList(fullReductionQueryWrapper);
        productParam.setProductFullReductionList(productFullReductionList);

        QueryWrapper<MemberPrice> priceQueryWrapper = new QueryWrapper<>();
        priceQueryWrapper.eq("product_id", productId);
        List<MemberPrice> memberPriceList = memberPriceMapper.selectList(priceQueryWrapper);
        productParam.setMemberPriceList(memberPriceList);

        QueryWrapper<SkuStock> skuStockQueryWrapper = new QueryWrapper<>();
        skuStockQueryWrapper.eq("product_id", productId);
        List<SkuStock> skuStockList = skuStockMapper.selectList(skuStockQueryWrapper);
        productParam.setSkuStockList(skuStockList);

        QueryWrapper<ProductAttributeValue> attributeValueQueryWrapper = new QueryWrapper<>();
        attributeValueQueryWrapper.eq("product_id", productId);
        List<ProductAttributeValue> productAttributeValueList = productAttributeValueMapper.selectList(attributeValueQueryWrapper);
        productParam.setProductAttributeValueList(productAttributeValueList);

        List<SubjectProductRelation> subjectProductRelationList = subjectService.getSubjectProductRelationByProductId(productId);
        productParam.setSubjectProductRelationList(subjectProductRelationList);

        List<PrefrenceAreaProductRelation> prefrenceAreaProductRelationList = prefrenceAreaService.getPrefrenceAreaProductRelationListByProductId(productId);
        productParam.setPrefrenceAreaProductRelationList(prefrenceAreaProductRelationList);

        return productParam;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void createProductParam(@Valid PmsProductParam productParam) {
        //TODO  创建商品实现
        ProductServiceImpl psProxy = (ProductServiceImpl) AopContext.currentProxy();
        psProxy.saveBaseProductInfo(productParam);
        psProxy.saveMemberPriceList(productParam.getMemberPriceList());
        psProxy.saveProductAttributeValueList(productParam.getProductAttributeValueList());
        psProxy.saveProductFullReductionList(productParam.getProductFullReductionList());
        psProxy.saveProductLadderList(productParam.getProductLadderList());
        psProxy.updateProductCategoryCount();
        prefrenceAreaService.savePrefrenceAreaProductRelationList(productParam.getPrefrenceAreaProductRelationList(), threadLocal.get().getId());
        subjectService.saveSubjectProductRelationList(productParam.getSubjectProductRelationList(), threadLocal.get().getId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateProductCategoryCount() {
        Product product = threadLocal.get();
        Long id = product.getId();
        productCategoryMapper.updateCountById(id);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveBaseProductInfo(@Valid PmsProductParam productParam) {
        ProductServiceImpl psProxy = (ProductServiceImpl) AopContext.currentProxy();
        psProxy.saveProduct(productParam);
        psProxy.saveSkuStockList(productParam.getSkuStockList());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveProduct(PmsProductParam productParam) {
        Product product = new Product();
        BeanUtils.copyProperties(productParam, product);
        Integer insert = baseMapper.insert(product);
        threadLocal.set(product);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveSkuStockList(List<SkuStock> list) {
        Product product = threadLocal.get();
        NumberFormat numberFormat = DecimalFormat.getNumberInstance();
        numberFormat.setMinimumIntegerDigits(2);
        numberFormat.setMaximumIntegerDigits(2);
        AtomicReference<Integer> i = new AtomicReference<>(0);
        list.forEach(skuStock -> {
            skuStock.setProductId(product.getId());
            String format = numberFormat.format(i.get());
            String code = "K_" + product.getId() + "_" + format;
            skuStock.setSkuCode(code);
            Integer insert = skuStockMapper.insert(skuStock);
            i.set(i.get() + 1);
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProductLadderList(List<ProductLadder> ladderList) {
        Product product = threadLocal.get();
        ladderList.forEach(productLadder -> {
            productLadder.setProductId(product.getId());
            Integer insert = productLadderMapper.insert(productLadder);
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProductFullReductionList(List<ProductFullReduction> productFullReductionList) {
        Product product = threadLocal.get();
        productFullReductionList.forEach(productFullReduction -> {
            productFullReduction.setId(product.getId());
            Integer insert = productFullReductionMapper.insert(productFullReduction);
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveMemberPriceList(List<MemberPrice> memberPriceList) {
        Product product = threadLocal.get();
        memberPriceList.forEach(memberPrice -> {
            memberPrice.setProductId(product.getId());
            Integer insert = memberPriceMapper.insert(memberPrice);
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProductAttributeValueList(List<ProductAttributeValue> productAttributeValueList) {
        Product product = threadLocal.get();
        productAttributeValueList.forEach(productAttributeValue -> {
            productAttributeValue.setProductId(product.getId());
            Integer insert = productAttributeValueMapper.insert(productAttributeValue);
        });
    }

}