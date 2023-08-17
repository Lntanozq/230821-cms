package com.briup.cms.aop;

import com.briup.cms.bean.Log;
import com.briup.cms.service.ILogService;
import com.briup.cms.util.JwtUtil;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 切面类
 * 1.如果实现AOP功能，添加aop依赖信息
 * 2.添加切入点规则
 * 3.添加通知 织入日志代码的位置
 * 4.运行任意的controller 代码 都会自动调用
 *   切点类中方法
 */
@Aspect //当前类是一个切面类
@Component// 当前类被spring IOC容器创建对象
public class LogAspect {
    @Autowired
    private ILogService logService;

    // 连接点 : 所有的方法称为连接点
    // 切入点: 部分连接点，根据切入规则
    // spring框架提供的表达式
    @Pointcut("execution(* com.briup.cms.web.controller.*.*(..)) && @annotation(Logging)")//使用@Pointcut编写哪些方法需要提供日志记录功能
    public void LogCutPoint(){
        // 该方法用来定义切入点的规则
        // 切入点规则的名称就是该方法名
        // 通过该表达式，表示 web层中所有的方法都作为切入点
        // 只要方法满足切入点规则，spring在程序运行过程中动态添加日志代码
        /*
         选择所有方法作为添加日志的方法，但当用户登录时，
         未提供日志信息中realName token。。。导致程序错误
           通过提供一个注解设置哪些方法可以添加日志功能
         */
    }

    //增强的代码添加到切入点的位置在哪里？
    @Before("LogCutPoint()") //在方法执行前执行代码
    public void before(){
        System.out.println("前置通知");
        //浏览器--请求报文--tomcat--->request对象
        //1.获取到浏览器访问系统的请求信息
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes)requestAttributes;
        HttpServletRequest request = sra.getRequest();

        //2.将请求信息封装到日志对象中
        Log log = new Log();
        //利用token信息提供username 和 realName
        String token = request.getHeader("token");
        String username = (String) JwtUtil.parseJWT(token).get("username");
        // 设置登录用户名
        log.setUsername(username);
        // 设置请求方式
        log.setRequestMethod(request.getMethod());
        // 设置请求uri
        log.setRequestUri(request.getServletPath());
        // 设置访问执行时间 此处不写 service层补充
        // log.setLogTime(LocalDateTime.now());

        //3.将日志保存数据库
        logService.save(log);
    }

    // 测试代码
  /*  @After("LogCutPoint()")
    public void after(){
        System.out.println("后置通知");
    }

    @Around("LogCutPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("开始环绕通知");
        Object obj = pjp.proceed();
        System.out.println("结束环绕通知");
        return obj;
    }*/
}
