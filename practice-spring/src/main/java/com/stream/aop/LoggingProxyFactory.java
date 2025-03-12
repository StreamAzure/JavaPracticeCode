package com.stream.aop;

import java.lang.reflect.Proxy;

public class LoggingProxyFactory {
    public static <T> T createProxy(T target, Class<T> interfaceType) {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                new Class<?>[] {interfaceType},
                new LoggingHandler(target)
        );
    }
}
