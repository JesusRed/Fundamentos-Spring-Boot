package com.fundamentos.springboot.fundamentos.bean;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

public class MyBeanWithDependencyImplement implements MyBeanWithDependency {
    Log LOGGER = LogFactory.getLog(MyBeanWithDependencyImplement.class);
    MyOperation myOperation;

    public MyBeanWithDependencyImplement(MyOperation myOperation) {
        this.myOperation = myOperation;
    }

    @Override
    public void printWithDependency() {
        LOGGER.info("Welcome to printWithDependency method");
        int number = 1;
        LOGGER.debug("Number: "+number+" is OK");
        System.out.println(myOperation.sum(number));
        System.out.println("Hello implement... AND DEPENDENCY");
    }
}
