package com.atguigu.gmall.admin.config;

import com.atguigu.gmall.to.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

/**
 * @author ：mei
 * @date ：Created in 2019/3/20 0020 下午 18:42
 * @description：全局AOP切面处理数据验证
 * @modified By：
 * @version: $
 */
@Component
@Aspect
@Slf4j
public class GmallValidatorAspect {

    @Around("execution(* com.atguigu.gmall.admin..controller..*.*(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();

        for (Object obj : args) {
            if(obj instanceof BindingResult){
                int count = ((BindingResult) obj).getErrorCount();
                if (count > 0){
                    CommonResult commonResult = new CommonResult().validateFailed((BindingResult) obj);
                    return commonResult;
                }
            }
        }
        Object proceed = proceedingJoinPoint.proceed(args);

        return proceed;
    }
}
