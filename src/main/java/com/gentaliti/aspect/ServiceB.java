package com.gentaliti.aspect;

import org.springframework.stereotype.Service;

@Service
public class ServiceB {

    public String call(){
        System.out.println("From Service A");
        return "B";
    }
}
