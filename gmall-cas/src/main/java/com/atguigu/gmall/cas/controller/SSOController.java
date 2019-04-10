package com.atguigu.gmall.cas.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.constant.RedisCacheConstant;
import com.atguigu.gmall.ums.entity.Member;
import com.atguigu.gmall.ums.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author ：mei
 * @date ：Created in 2019/4/1 0001 下午 22:34
 * @description：
 * @modified By：
 * @version: $
 */
@Controller
public class SSOController {

    @Reference
    MemberService memberService;
    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping("/login.html")
    public String loginPage(String url, @CookieValue(value = "gmallsso", required = false) String gmallsso) {
        return "";
    }


    @PostMapping("/logintosys")
    public String login(@RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password,
                        @RequestParam(value = "url") String url,
                        HttpSession session,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        String token = UUID.randomUUID().toString().replace("-", "");
        Member member = memberService.login(username, password);
        if (member != null) {
            String memberJson = JSON.toJSONString(member);
            redisTemplate.opsForValue().set(RedisCacheConstant.USER_INFO_CACHE_KEY + token, memberJson, RedisCacheConstant.USER_INFO_TIMEOUT, TimeUnit.DAYS);
            return "redirect:" + url + "?token=" + token;
        } else {
            String referer = request.getHeader("Referer");
            return "redirect:" + referer + "?token=" + "";
        }

    }
}
