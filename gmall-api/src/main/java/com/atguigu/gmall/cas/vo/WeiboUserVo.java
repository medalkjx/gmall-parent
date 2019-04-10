package com.atguigu.gmall.cas.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ：mei
 * @date ：Created in 2019/4/1 0001 下午 21:27
 * @description：微博用户信息
 * @modified By：
 * @version: $
 */
@Data
public class WeiboUserVo implements Serializable {
    private Long id;
    private String name;
    private String province;
    private String location;
    private String profile_image_url;

}
