package com.stream.aop;

import com.stream.common.UserService;
import com.stream.common.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserService userService = LoggingProxyFactory.createProxy(
                new UserServiceImpl(),
                UserService.class
        );
        String userName = userService.getUserName(1);
        System.out.println(userName);
    }
}
