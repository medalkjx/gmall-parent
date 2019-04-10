package com.atguigu.gmall.cas.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：mei
 * @date ：Created in 2019/4/1 0001 下午 19:22
 * @description：微博App接入参数
 * @modified By：
 * @version: $
 */
@ConfigurationProperties(prefix = "oauth.weibo")
@Configuration
@Data
public class WeiboOAuthConfig {

    private String appKey;
    private String appSecret;
    private String authSuccessUrl;
    private String authSuccessFail;
    private String authPage;
    private String accessTokenPage;
}
