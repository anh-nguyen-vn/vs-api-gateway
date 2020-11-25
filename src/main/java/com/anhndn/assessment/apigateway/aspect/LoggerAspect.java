package com.anhndn.assessment.apigateway.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

@Aspect
@Configuration
@Slf4j
public class LoggerAspect {

    @Around("within(com.anhndn.assessment.apigateway.*) && @annotation(LogAround)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("===== Start: {} =====", this.getMessage(joinPoint));
        try {
            Object returning = joinPoint.proceed();
            log.info("===== End with success: {} =====", this.getMessage(joinPoint));
            return returning;
        } catch (Throwable e) {
            log.error("There is an error occur in process", e);
            log.info("===== End with failure: {} =====", this.getMessage(joinPoint));
            throw e;
        }
    }

    private String getMessage(JoinPoint joinPoint) throws NoSuchMethodException {
        Method interfaceMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Method implementationMethod = joinPoint.getTarget().getClass().getMethod(interfaceMethod.getName(), interfaceMethod.getParameterTypes());

        String message = null;
        if (implementationMethod.isAnnotationPresent(LogAround.class)) {
            LogAround logAround = implementationMethod.getAnnotation(LogAround.class);
            message = logAround.message();
        }

        return message;
    }

}
