package com.atguigu.gmall.cas.vo;

import lombok.Data;

/**
 * @author ：mei
 * @date ：Created in 2019/4/1 0001 下午 20:28
 * @description：微博返回参数
 * @modified By：
 * @version: $
 */
@Data
public class WeiboAccessTokenVo extends AccessTokenVo {

    private String remind_in;
    private String expires_in;
    private String uid;
    private String isRealName;

}
