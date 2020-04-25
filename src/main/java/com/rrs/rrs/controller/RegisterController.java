package com.rrs.rrs.controller;


import com.alibaba.fastjson.JSONObject;
import com.rrs.rrs.dto.ResultDTO;
import com.rrs.rrs.exception.CustomizeErrorCode;
import com.rrs.rrs.model.User;
import com.rrs.rrs.provider.ZhenziProvider;
import com.rrs.rrs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class RegisterController {
    @Autowired
    UserService userService;
    @Autowired
    ZhenziProvider zhenziProvider;


    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("backgroundPic", "/images/background/restaurant4.jpg");
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

    @ResponseBody//把页面转化成其它结构
    @RequestMapping(value = "/register/phone",method = RequestMethod.POST)
    public Object post(@RequestBody String data,
                       HttpServletRequest request){
        //接受手机号
        JSONObject dataJson = JSONObject.parseObject(data);
        String phone=dataJson.getString("phone");
        try {
            //产生验证码
            if (zhenziProvider.sendVerifyCode(request,phone))
                return ResultDTO.okOf();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultDTO.errorOf(CustomizeErrorCode.VERIFYCODE_SEND_FAIL);
    }

    @ResponseBody
    @RequestMapping(value = "/register/phone/verify",method = RequestMethod.POST)
    public Object verify(@RequestBody JSONObject dataJson,
                         HttpServletRequest request){
        return userService.getVerify(dataJson, request);
    }

    //绑定手机号码
    @ResponseBody
    @RequestMapping(value = "/register/phone/binding",method = RequestMethod.POST)
    public Object binding(@RequestBody JSONObject dataJson,
                          HttpServletRequest request){
        ResultDTO result=(ResultDTO)userService.getVerify(dataJson, request);
        if (result.getCode()==200){//验证成功进行绑定
            userService.bindingPhone(dataJson, request);
        }

        return result;
    }


}
