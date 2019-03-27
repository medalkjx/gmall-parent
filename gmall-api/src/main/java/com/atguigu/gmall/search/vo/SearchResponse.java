package com.atguigu.gmall.search.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：mei
 * @date ：Created in 2019/3/26 0026 下午 19:16
 * @description：
 * @modified By：
 * @version: $
 */
@Data
public class SearchResponse implements Serializable {

    private SearchResponseAttrVo brand;//品牌
    private SearchResponseAttrVo catelog;//分类
    //所有商品的顶头显示的筛选属性
    private List<SearchResponseAttrVo> attrs = new ArrayList<>();

    //检索出来的商品信息
    private List<EsProduct> products = new ArrayList<>();

    private Long total;//总记录数
    private Integer pageSize;//每页显示的内容
    private Integer pageNum;//当前页面


}
