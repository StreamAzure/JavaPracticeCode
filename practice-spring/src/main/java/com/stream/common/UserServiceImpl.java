package com.stream.common;

import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.jdbc.datasource.DataSourceUtils.getConnection;

@Service
public class UserServiceImpl implements UserService{
    static Map<Long, String> users = new HashMap<>();
    static {
        users.put(1L, "Mike");
        users.put(2L, "John");
        users.put(3L, "Amy");
        users.put(4L, "world");
    }
    @Override
    public String getUserName(long userId) {
        return users.get(userId);
    }
}
