package com.atguigu.gmall.admin.config;

import com.atguigu.gmall.to.CommonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author ：mei
 * @date ：Created in 2019/3/20 0020 下午 19:14
 * @description：全局统一异常处理
 * @modified By：
 * @version: $
 */
@RestControllerAdvice
public class GmallGlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public CommonResult exception(Exception e){

        return new CommonResult().failed().validateFailed(e.getMessage());
    }
}
