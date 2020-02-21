package com.rrs.rrs.interceptors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Autowired
    private SessionInterceptor sessionInterceptor;
    @Autowired
    private UserInterceptor userInterceptor;
    @Autowired
    private AdminInterceptor adminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
        List<String> paths=new ArrayList();
        paths.add("/profile/**");
        registry.addInterceptor(userInterceptor).addPathPatterns(paths);
        registry.addInterceptor(adminInterceptor).addPathPatterns("/manage/**");

    }
}
