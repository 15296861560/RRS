package com.rrs.rrs.interceptors;

import com.rrs.rrs.mapper.AdminMapper;
import com.rrs.rrs.mapper.UserMapper;
import com.rrs.rrs.model.Admin;
import com.rrs.rrs.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AdminInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Admin admin =(Admin)request.getSession().getAttribute("admin");
        if (admin==null)return false;//判断session中是否有admin
        else {
            admin=adminMapper.findById(admin.getAdminId());
        }
        if (admin==null)return false;//判断该用户是否是数据库中的管理员
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
