package com.atguigu.gmall.ums.service;

import com.atguigu.gmall.ums.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface AdminService extends IService<Admin> {


    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    Admin login(String username, String password);

    /**
     * 根据用户名获取用户信息
     * @param userName
     * @return
     */
    Admin getAdminByUsername(String userName);
}
