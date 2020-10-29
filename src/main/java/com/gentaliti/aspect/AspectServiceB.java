package com.gentaliti.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectServiceB {

    @AfterReturning(pointcut = "execution(* com.gentaliti.aspect.ServiceB.call(..))", returning = "s")
    public void afterCreation(String s) {
        System.out.println("Aspect A");
    }
}
