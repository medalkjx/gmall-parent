package com.atguigu.gmall.cas.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ：mei
 * @date ：Created in 2019/4/1 0001 下午 21:23
 * @description：accessTokenVo
 * @modified By：
 * @version: $
 */
@Data
public class AccessTokenVo implements Serializable {
    private String access_token;
}
