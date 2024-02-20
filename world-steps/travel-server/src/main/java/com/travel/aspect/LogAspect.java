package com.travel.aspect;

import com.alibaba.fastjson.JSONObject;
import com.travel.context.BaseContext;
import com.travel.entity.OperateLog;
import com.travel.mapper.OperateLogMapper;
import com.travel.properties.JwtProperties;
import com.travel.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class LogAspect {
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Around("@annotation(com.travel.annotation.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable{

        // Get operator id
        String jwt = request.getHeader("token");
//        Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), jwt);
//        Integer operateUser = (Integer) claims.get("id");
//        Integer operateUser = Math.toIntExact(BaseContext.getCurrentId());
        Integer operateUser = null;
        if (BaseContext.getCurrentId() != null) {
            operateUser = Math.toIntExact(BaseContext.getCurrentId());
        }

        // Get operate time
        LocalDateTime operateTime = LocalDateTime.now();

        // Get class name
        String className = joinPoint.getTarget().getClass().getName();

        // Get method name
        String methodName = joinPoint.getSignature().getName();

        // Get params
        Object[] args = joinPoint.getArgs();
        String methodParams = Arrays.toString(args);

        long begin = System.currentTimeMillis();
        // Call the original method
        // Return value of the method
        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();

        String returnValue = JSONObject.toJSONString(result);

        // Cost time
        Long costTime = end - begin;

        // Record operate log
        OperateLog operateLog = new OperateLog(null, operateUser, operateTime, className, methodName, methodParams, returnValue, costTime);
        operateLogMapper.insert(operateLog);

        log.info("AOP operate log: {}", operateLog);

        return result;
    }
}
