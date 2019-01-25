package com.muse.pay.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Administrator
 * @Date: 2018 2018/7/26 15 59
 **/

public class MyIntercept implements HandlerInterceptor {
    private Logger log = LoggerFactory.getLogger("MyIntercept");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long start = System.currentTimeMillis();
        request.setAttribute("t", start);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (request.getAttribute("t") != null) {
            long start = (long) request.getAttribute("t");
            if (start > 0) {
                long end = System.currentTimeMillis();
                long time = end - start;
                log.info("访问[" + request.getRequestURI() + "]耗时=" + time);
            }
            request.removeAttribute("t");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


}
