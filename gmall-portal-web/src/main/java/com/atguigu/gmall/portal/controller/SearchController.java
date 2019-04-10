package com.atguigu.gmall.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.search.service.GmallSearchService;
import com.atguigu.gmall.search.vo.SearchParam;
import com.atguigu.gmall.search.vo.SearchResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author ：mei
 * @date ：Created in 2019/3/26 0026 下午 20:08
 * @description：
 * @modified By：
 * @version: $
 */
@CrossOrigin
@RestController
public class SearchController {
    @Reference
    GmallSearchService searchService;


    @GetMapping("/search")
    public SearchResponse search(SearchParam searchParam) throws IOException {
        SearchResponse searchResponse = searchService.searchProduct(searchParam);
        return searchResponse;
    }

}
