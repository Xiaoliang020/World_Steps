package com.travel.filter;

import com.alibaba.fastjson.JSONObject;
import com.travel.properties.JwtProperties;
import com.travel.result.Result;
import com.travel.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
//@WebFilter(urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Get URL
        String url = request.getRequestURL().toString();
        log.info("Requested URL: {}", url);


        // If login, give access
        if (url.contains("login")) {
            log.info("Login request...");
            filterChain.doFilter(request, response);
            return;
        }

        // Get the token
        String jwt = request.getHeader("token");

        // Check if exists, if not, return error
        if (!StringUtils.hasLength(jwt)) {
            log.info("Token in header is null");
            Result error = Result.error("NOT_LOGIN");
            // Change object to json
            String notLogin = JSONObject.toJSONString(error);

            response.getWriter().write(notLogin);
            return;
        }

        // Parse the token
        try {
            JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), jwt);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Token parse failed");
            // parse failed
            Result error = Result.error("NOT_LOGIN");
            // Change object to json
            String notLogin = JSONObject.toJSONString(error);

            response.getWriter().write(notLogin);
            return;
        }

        // Access
        log.info("Valid token");
        filterChain.doFilter(request, response);
    }
}
