package com.rrs.rrs.controller;


import com.rrs.rrs.dto.AccessTokenDTO;
import com.rrs.rrs.dto.GithubUser;
import com.rrs.rrs.model.User;
import com.rrs.rrs.provider.GithubProvider;
import com.rrs.rrs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;
    //从配置文件中注入相应值
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;


    @Autowired
    private UserService userService;
    @GetMapping("/github/callback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state ,
                               HttpServletResponse response){

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);//通过provider获取token
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser!=null&&githubUser.getId()!=null){
            //加载用户数据
            User user=new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setUserName(githubUser.getName());
            user.setCode("github"+String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(System.currentTimeMillis());
//            user.setAvatarUrl(githubUser.getAvatarUrl());
            userService.createOrUpdate(user);


            //将token写入cookie
            response.addCookie(new Cookie("token",token));
            //自动注册登录成功进入菜单页面
            return "redirect:/food";
        }else {
            //登录失败，重新登陆
            log.error("callback get github error,{}",githubUser);
            return "redirect:/";
        }
    }

}
