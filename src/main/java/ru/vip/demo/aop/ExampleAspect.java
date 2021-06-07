package ru.vip.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExampleAspect {

    @Around("@annotation(ru.vip.demo.aop.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        final long start = System.currentTimeMillis();
        //final long start = System.nanoTime();
        final Object proceed = joinPoint.proceed();
        //final long executionTime = System.nanoTime() - start;
        final long executionTime = System.currentTimeMillis() - start;
        System.out.println(" Время исполнения = " + executionTime + "ms");

        //System.out.println(joinPoint.getSignature() + " Время исполнения=" + executionTime + "ms");
        return  proceed;
    }
}
