package com.stream.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class LoggingHandler implements InvocationHandler {
    private Object target;

    public LoggingHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        // 检查方法是否包含 log 注释
        if (method.isAnnotationPresent(log.class)) {
            System.out.println("开始执行方法……");
            result = method.invoke(target, args);
            System.out.println("方法执行完毕");
        }
        else {
            result = method.invoke(target, args);
        }
        return result;
    }
}
