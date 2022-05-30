package com.fundamentos.springboot.fundamentos.component;

import org.springframework.stereotype.Component;

@Component
public class ComponentToImplement implements ComponentDependency {
    @Override
    public void greetings() {
        System.out.println("Hello world V2");
    }
}
