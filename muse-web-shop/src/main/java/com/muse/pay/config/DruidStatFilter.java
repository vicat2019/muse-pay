package com.muse.pay.config;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * @Author: Administrator
 * @Date: 2018 2018/7/22 19 38
 **/
@WebFilter(filterName = "druidWebStatFilter", urlPatterns = "/*",
        initParams = {
                @WebInitParam(name = "exclusions", value = "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*")// 忽略资源
        })
public class DruidStatFilter {
}
