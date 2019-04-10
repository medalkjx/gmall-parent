package com.atguigu.gmall.ums.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.cas.vo.AccessTokenVo;
import com.atguigu.gmall.cas.vo.WeiboAccessTokenVo;
import com.atguigu.gmall.cas.vo.WeiboUserVo;
import com.atguigu.gmall.constant.SocialConstant;
import com.atguigu.gmall.ums.entity.Member;
import com.atguigu.gmall.ums.entity.MemberSocial;
import com.atguigu.gmall.ums.mapper.MemberMapper;
import com.atguigu.gmall.ums.mapper.MemberSocialMapper;
import com.atguigu.gmall.ums.service.MemberSocialService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mei
 * @since 2019-04-01
 */
@Component
@Service
public class MemberSocialServiceImpl extends ServiceImpl<MemberSocialMapper, MemberSocial> implements MemberSocialService {

    @Autowired
    MemberSocialMapper memberSocialMapper;
    @Autowired
    MemberMapper memberMapper;

    @Transactional
    @Override
    public Member getMemberInfo(AccessTokenVo tokenVo) {
        Member member = null;
        if (tokenVo instanceof WeiboAccessTokenVo) {
            WeiboAccessTokenVo token = (WeiboAccessTokenVo) tokenVo;
            member = memberSocialMapper.getMemberInfo(token.getUid());
            if (member == null) {
                Member registMember = new Member();
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet("https://api.weibo.com/2/users/show.json?access_token=" + token.getAccess_token() + "&uid=" + token.getUid());
                try {
                    CloseableHttpResponse execute = httpClient.execute(httpGet);
                    HttpEntity entity = execute.getEntity();
                    String useMessage = EntityUtils.toString(entity);
                    WeiboUserVo userVo = JSON.parseObject(useMessage, WeiboUserVo.class);
                    registMember.setIcon(userVo.getProfile_image_url());
                    registMember.setNickname(userVo.getName());
                    List<MemberSocial> memberSocials = memberSocialMapper.selectAccessTokenForUpdate(token.getAccess_token());
                    if (memberSocials != null && memberSocials.size() > 0) {
                        memberMapper.selectById(memberSocials.get(0).getUserId());
                    } else {
                        memberMapper.insert(registMember);
                        MemberSocial memberSocial = new MemberSocial();
                        memberSocial.setType(SocialConstant.SocialTypeEnum.WEIBO.getType());
                        memberSocial.setAccessToken(token.getAccess_token());
                        memberSocial.setUid(userVo.getId().toString());
                        memberSocial.setUserId(registMember.getId());
                        memberSocialMapper.insert(memberSocial);
                    }

                } catch (IOException e) {
                }
            } else {
                return member;
            }
        }
        return member;
    }
}
