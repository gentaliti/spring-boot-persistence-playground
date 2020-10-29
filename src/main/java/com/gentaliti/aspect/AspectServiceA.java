package com.gentaliti.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectServiceA {
    private ServiceB serviceB;

    public AspectServiceA(ServiceB serviceB) {
        this.serviceB = serviceB;
    }

    @AfterReturning(pointcut = "execution(* com.gentaliti.aspect.ServiceA.call(..))", returning = "s")
    public void afterCreation(String s) {
        System.out.println("Aspect A");
        serviceB.call();
    }
}
