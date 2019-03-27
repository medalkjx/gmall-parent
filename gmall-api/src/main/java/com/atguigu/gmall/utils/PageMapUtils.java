package com.atguigu.gmall.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：mei
 * @date ：Created in 2019/3/22 0022 下午 18:57
 * @description：分页组装
 * @modified By：
 * @version: $
 */
public class PageMapUtils {

    public static Map<String ,Object> getPageMap(IPage page){
        Map<String, Object> result = new HashMap<>();
        result.put("pageSize", page.getSize());
        result.put("totalPage", page.getPages());
        result.put("total", page.getTotal());
        result.put("pageNum", page.getCurrent());
        result.put("list", page.getRecords());
        return result;
    }
}
