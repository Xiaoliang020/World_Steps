package com.travel.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

//@WebFilter(urlPatterns = "/login")
public class DemoFilter implements Filter {
    @Override // Only called once
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init 执行初始化方法");
    }

    @Override // Called after intercept request
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("拦截到了请求");
        // 放行
        filterChain.doFilter(servletRequest, servletResponse);

        System.out.println("放行后逻辑");
    }

    @Override // Only called once
    public void destroy() {
        System.out.println("destroy 执行销毁方法");
    }
}
