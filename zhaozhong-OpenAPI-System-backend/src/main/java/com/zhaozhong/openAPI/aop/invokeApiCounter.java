package com.zhaozhong.openAPI.aop;

import com.zhaozhong.openAPI.annotation.InvokeApiCount;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Aspect
@Component
public class invokeApiCounter {

    @Around("@annotation(invokeApiCount)")
    public Object countInvoke(ProceedingJoinPoint joinPoint, InvokeApiCount invokeApiCount) throws Throwable {

        Object result = joinPoint.proceed();
        
        return result;
    }

}
