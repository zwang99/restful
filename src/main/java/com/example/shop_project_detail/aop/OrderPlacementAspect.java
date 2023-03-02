package com.example.shop_project_detail.aop;

import com.example.shop_project_detail.domain.response.UserOrderResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Aspect
@Component
public class OrderPlacementAspect {
    private Logger logger = LoggerFactory.getLogger(OrderPlacementAspect.class);

    @After("execution(* com.example.shop_project_detail.controller.UserPurchaseController.placeOrder(..))")
    public void logOrderPLaced(){
        logger.info("Order placed at: {}", LocalDateTime.now());
    }

    @After("execution(* com.example.shop_project_detail.service.OrderService.cancelOrder(..))")
    public void logOrderCanceled(){
        logger.info("Order canceled at: {}", LocalDateTime.now());
    }

    @After("execution(* com.example.shop_project_detail.service.OrderService.completeOrder(..))")
    public void logOrderCompleted(){
        logger.info("Order completed at: {}", LocalDateTime.now());
    }

    @Around("execution(* com.example.shop_project_detail.controller.UserPurchaseController.getOrderDetail(..))")
    public UserOrderResponse logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        System.out.println(joinPoint.getSignature() + " executed in " + (endTime - startTime) + "ms");
        return (UserOrderResponse) result;
    }

    @Around("execution(* com.example.shop_project_detail.controller.UserPurchaseController.getAsyncOrderDetail(..))")
    public CompletableFuture<UserOrderResponse> logAsyncExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        System.out.println(joinPoint.getSignature() + " executed in " + (endTime - startTime) + "ms");
        return (CompletableFuture<UserOrderResponse>) result;
    }
}
