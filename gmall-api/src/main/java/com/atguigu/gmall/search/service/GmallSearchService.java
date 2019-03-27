package com.atguigu.gmall.search.service;

import com.atguigu.gmall.search.vo.EsProduct;
import com.atguigu.gmall.search.vo.SearchParam;
import com.atguigu.gmall.search.vo.SearchResponse;

import java.io.IOException;

public interface GmallSearchService {

    /**
     * 保存数据到es
     * @param esProduct
     * @return
     */
    boolean saveProductInfoToES(EsProduct esProduct);

    /**
     * 检索商品
     * @param searchParam
     * @return
     */
    SearchResponse searchProduct(SearchParam searchParam) throws IOException;
}
