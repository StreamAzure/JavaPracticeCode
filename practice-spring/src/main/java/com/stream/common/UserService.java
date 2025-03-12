package com.stream.common;

import com.stream.aop.log;


public interface UserService {
    @log
    String getUserName(long userId);
}
