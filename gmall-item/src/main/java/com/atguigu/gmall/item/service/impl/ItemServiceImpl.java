package com.atguigu.gmall.item.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.item.vo.ItemService;
import com.atguigu.gmall.item.vo.ProductAllInfos;
import com.atguigu.gmall.pms.entity.Product;
import com.atguigu.gmall.pms.entity.SkuStock;
import com.atguigu.gmall.pms.service.ProductService;
import com.atguigu.gmall.pms.service.SkuStockService;
import com.atguigu.gmall.search.vo.EsProductAttributeValue;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ：mei
 * @date ：Created in 2019/3/29 0029 下午 19:31
 * @description：
 * @modified By：
 * @version: $
 */
@Service
@Component
public class ItemServiceImpl implements ItemService {
    @Reference
    SkuStockService skuStockService;
    @Reference
    ProductService productService;

    @Override
    public ProductAllInfos getInfo(Long skuId) {
        ProductAllInfos infos = new ProductAllInfos();
        SkuStock skuStock = skuStockService.getById(skuId);
        Long productId = skuStock.getProductId();
        Product product = productService.getProductByIdFromCache(productId);

        List<SkuStock> skuStocks = skuStockService.getAllSkuInfoByProductId(productId);

        List<EsProductAttributeValue> saleAttr = productService.getProductSaleAttr(productId);

        List<EsProductAttributeValue> baseAttr = productService.getProductBaseAttr(productId);

        infos.setSaleAttr(saleAttr);
        infos.setBaseAttr(baseAttr);
        infos.setProduct(product);
        infos.setSkuStocks(skuStocks);
        infos.setSkuStock(skuStock);
        return infos;
    }
}
