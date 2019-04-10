package com.atguigu.gmall.cas.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.cas.config.WeiboOAuthConfig;
import com.atguigu.gmall.cas.vo.WeiboAccessTokenVo;
import com.atguigu.gmall.constant.RedisCacheConstant;
import com.atguigu.gmall.ums.entity.Member;
import com.atguigu.gmall.ums.service.MemberSocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @author ：mei
 * @date ：Created in 2019/4/1 0001 下午 19:10
 * @description：OAuth2社交登录
 * @modified By：
 * @version: $
 */
@CrossOrigin
@Controller
public class OAuth2Controller {

    @Autowired
    WeiboOAuthConfig config;

    RestTemplate restTemplate = new RestTemplate();
    @Reference
    MemberSocialService memberSocialService;
    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping("/register/authorization")
    public String registerAuthorization(@RequestParam("authType") String authType,
                                        @RequestParam("url") String url,
                                        HttpSession session) {
        session.setAttribute("url", url);
        if ("weibo".equals(authType)) {
            return "redirect:" + config.getAuthPage();
        }
        return "redirect" + config.getAuthPage();
    }

    @GetMapping("/auth/success")
    public String codeGetToken(@RequestParam("code") String code, HttpSession session) {
        String authPage = config.getAccessTokenPage() + "&code=" + code;
        WeiboAccessTokenVo tokenVo = restTemplate.postForObject(authPage, null, WeiboAccessTokenVo.class);
        Member memberInfo = memberSocialService.getMemberInfo(tokenVo);

        String url = (String) session.getAttribute("url");
        String token = UUID.randomUUID().toString();
        String memberInfoJson = JSON.toJSONString(memberInfo);
        redisTemplate.opsForValue().set(RedisCacheConstant.USER_INFO_CACHE_KEY + token, memberInfoJson);
        return "redirect:" + url + "?token=" + token;
    }

    @ResponseBody
    @GetMapping("/userinfo")
    public Member getUserInfo(String token) {
        String memberInfo = redisTemplate.opsForValue().get(RedisCacheConstant.USER_INFO_CACHE_KEY);
        Member member = JSON.parseObject(memberInfo, Member.class);
        return member;
    }

}
