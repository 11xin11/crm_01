package com.yjxxt.crm.aop;

import com.yjxxt.crm.annotation.RequiredPermission;
import com.yjxxt.crm.exception.NoAuthException;
import com.yjxxt.crm.exception.NoLoginException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Component
@Aspect
public class PermissionProxy {

    @Resource
    private HttpSession session;

    @Around(value = "@annotation(com.yjxxt.crm.annotation.RequiredPermission)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        //判断是否登录
        List<String> permission=(List<String>) session.getAttribute("permission");
        if (permission==null||permission.size()==0){
            throw new NoLoginException("未登录");
        }
        //判断是否有访问目标资源的权限码
        MethodSignature methodSignature=(MethodSignature)pjp.getSignature();
        RequiredPermission requiredPermission = methodSignature.getMethod().getDeclaredAnnotation(RequiredPermission.class);
        //比对
        if (!(permission.contains(requiredPermission.code()))){
            throw  new NoAuthException("无权限访问！");
        }
        Object result=pjp.proceed();
        return result;

    }
}
