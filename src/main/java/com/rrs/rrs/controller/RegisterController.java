package com.rrs.rrs.controller;


import com.rrs.rrs.exception.CustomizeErrorCode;
import com.rrs.rrs.model.User;
import com.rrs.rrs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class RegisterController {
    @Autowired
    UserService userService;

    @GetMapping("/register")
    public String register(Model model){

        return "register";
    }

    @PostMapping("/register")// post方法给你请求
    public String doRegister(
            @RequestParam(value = "userName",required = false)String userName,
            @RequestParam(value = "phone",required = false)String phone,
            @RequestParam(value ="password",required = false)String password,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model){
        String result = userService.register(phone,userName,password);//进行注册
        if (result.equals("success")){
            User user= userService.findByPhone(phone);
            request.getSession().setAttribute("user",user);
            //将token写入cookie
            response.addCookie(new Cookie("token",user.getToken()));
            //重定向回首页
            model.addAttribute("tip","恭喜注册成功");
            model.addAttribute("src","/food");
            return "tip";
        }else if (result.equals(CustomizeErrorCode.REGISTER_FAIL_PHONE_NOT_FOUND.getMessage())){
            model.addAttribute("errorMessage",result);
            model.addAttribute("errorCode",CustomizeErrorCode.REGISTER_FAIL_PHONE_NOT_FOUND.getCode());
            return "error";
        }else if (result.equals(CustomizeErrorCode.REGISTER_FAIL_PHONE_REGISTERED.getMessage())){
            model.addAttribute("errorMessage",result);
            model.addAttribute("errorCode",CustomizeErrorCode.REGISTER_FAIL_PHONE_REGISTERED.getCode()
            );
            return "error";
        }

        model.addAttribute("errorMessage",CustomizeErrorCode.UNKNOWN_ERROR);
        return "error";
    }


}
