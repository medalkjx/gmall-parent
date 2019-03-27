package com.atguigu.gmall.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.search.constant.EsConstant;
import com.atguigu.gmall.search.service.GmallSearchService;
import com.atguigu.gmall.search.vo.EsProduct;
import com.atguigu.gmall.search.vo.SearchParam;
import com.atguigu.gmall.search.vo.SearchResponse;
import com.atguigu.gmall.search.vo.SearchResponseAttrVo;
import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author ：mei
 * @date ：Created in 2019/3/26 0026 下午 19:12
 * @description：
 * @modified By：
 * @version: $
 */
@Component
@Service
public class GmallSearchServiceImpl implements GmallSearchService {

    @Autowired
    JestClient jestClient;

    @Override
    public boolean saveProductInfoToES(EsProduct esProduct) {
        Index index = new Index.Builder(esProduct)
                .index(EsConstant.ES_PRODUCT_INDEX)
                .type(EsConstant.ES_PRODUCT_TYPE)
                .id(esProduct.getId().toString())
                .build();
        DocumentResult execute = null;
        try {
            execute = jestClient.execute(index);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return execute.isSucceeded();
    }

    @Override
    public SearchResponse searchProduct(SearchParam searchParam) throws IOException {
        //根据页面传递的参数构建检索的DSL语句
        String queryDSL = buildSearchDsl(searchParam);
        Search search = new Search.Builder(queryDSL).build();
        //执行查询
        SearchResult result = jestClient.execute(search);
        //封装和分析查询结果
        SearchResponse response = buildSearchResult(result);
        //封装分页信息
        response.setPageNum(searchParam.getPageNum());
        response.setPageSize(searchParam.getPageSize());
        response.setTotal(result.getTotal());

        return response;
    }

    /**
     * 封装检索结果
     *
     * @param result
     * @return
     */
    private SearchResponse buildSearchResult(SearchResult result) {
        SearchResponse searchResponse = new SearchResponse();
        //分装所有的商品信息
        List<SearchResult.Hit<EsProduct, Void>> hits = result.getHits(EsProduct.class);
        for (SearchResult.Hit<EsProduct, Void> hit : hits) {
            EsProduct source = hit.source;
            searchResponse.getProducts().add(source);
        }
        //封装属性信息
        //封装品牌
        MetricAggregation aggregations = result.getAggregations();

        SearchResponseAttrVo brandId = new SearchResponseAttrVo();
        brandId.setName("品牌");
        //获取到品牌
        aggregations.getTermsAggregation("brandIdAgg").getBuckets().forEach(brand -> {
            brand.getTermsAggregation("brandNameAgg").getBuckets().forEach(brandName -> {
                String key = brandName.getKey();
                brandId.getValue().add(key);
            });
        });
        searchResponse.setBrand(brandId);
        //获取到分类
        SearchResponseAttrVo category = new SearchResponseAttrVo();
        category.setName("分类");
        aggregations.getTermsAggregation("categoryIdAgg")
                .getBuckets().forEach(categoryId -> {
            categoryId.getTermsAggregation("productCategoryNameAgg")
                    .getBuckets().forEach(categoryName -> {
                String key = categoryName.getKey();
                category.getValue().add(key);
            });
        });
        searchResponse.setCatelog(category);
        //获取属性
        TermsAggregation termsAggregation = aggregations.getChildrenAggregation("productAttrAgg")
                .getChildrenAggregation("productAttrIdAgg")
                .getTermsAggregation("productAttrIdAgg");

        List<TermsAggregation.Entry> buckets = termsAggregation.getBuckets();
        buckets.forEach(productAttr -> {
            SearchResponseAttrVo attrVo = new SearchResponseAttrVo();
            attrVo.setProductAttributeId(Long.parseLong(productAttr.getKey()));
            productAttr.getTermsAggregation("productAttrNameAgg")
                    .getBuckets().forEach(attrName -> {
                attrVo.setName(attrName.getKey());
                attrName.getTermsAggregation("productAttrValueAgg")
                        .getBuckets().forEach(attrValue -> {
                    attrVo.getValue().add(attrValue.getKey());
                });
            });
            searchResponse.getAttrs().add(attrVo);
        });
        return searchResponse;
    }

    /**
     * 构建检索条件
     *
     * @param searchParam
     * @return
     */
    private String buildSearchDsl(SearchParam searchParam) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //查询
        searchSourceBuilder.query(QueryBuilders.matchQuery("name", searchParam.getKeyword()));

        //过滤
        //聚合
        //聚合品牌信息
        TermsAggregationBuilder brandAggs = AggregationBuilders.terms("brandIdAgg")
                .field("brandId")
                .size(100)
                .subAggregation(
                        AggregationBuilders.terms("brandNameAgg")
                                .field("brandName")
                                .size(100)
                );
        searchSourceBuilder.aggregation(brandAggs);
        //聚合分类信息
        TermsAggregationBuilder categoryAggs = AggregationBuilders.terms("categoryIdAgg")
                .field("productCategoryId")
                .size(100)
                .subAggregation(
                        AggregationBuilders.terms("productCategoryNameAgg")
                                .field("productCategoryName")
                                .size(100)
                );
        searchSourceBuilder.aggregation(categoryAggs);
        //属性聚合
        FilterAggregationBuilder filter = AggregationBuilders.filter("productAttrIdAgg",
                QueryBuilders.termQuery("attrValueList.type", "1"));
        filter.subAggregation(AggregationBuilders.terms("productAttrIdAgg")
                .field("attrValueList.productAttributeId")
                .size(100)
                .subAggregation(
                        AggregationBuilders.terms("productAttrNameAgg")
                                .field("attrValueList.name")
                                .size(100)
                                .subAggregation(
                                        AggregationBuilders.terms("productAttrValueAgg")
                                                .field("attrValueList.value")
                                                .size(100)
                                )
                )
        );
        //属性聚合
        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("productAttrAgg", "attrValueList")
                .subAggregation(filter);
        searchSourceBuilder.aggregation(attrAgg);
        //高亮
        if (searchParam.getKeyword() != null) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("name").preTags("<b style = 'color:red'>").postTags("</b>");
            searchSourceBuilder.highlighter(highlightBuilder);
        }
        //分页
        searchSourceBuilder.from((searchParam.getPageNum() - 1) * searchParam.getPageSize());
        searchSourceBuilder.size(searchParam.getPageSize());

        System.out.println(searchSourceBuilder.toString());
        return searchSourceBuilder.toString();
    }
}
