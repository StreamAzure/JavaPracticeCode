package com.stream.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

@WebFilter(urlPatterns = "/*")
public class CountFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        HttpSession session = request.getSession();
        Integer count = (Integer) session.getAttribute("count");
        count = Objects.isNull(count) ? 1 : count + 1;
        session.setAttribute("count", count);
        // 统计 uri 访问次数
        System.out.println("uri: " + uri + " ip: " + ip);
        // 放行
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
