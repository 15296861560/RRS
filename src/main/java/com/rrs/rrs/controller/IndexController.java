package com.rrs.rrs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("backgroundPic", "/images/background/restaurant4.jpg");
        model.addAttribute("slogan", "温暖你的是服务，感动你的是美食");
        return "index";
    }

    @GetMapping("/noLogin")
    public String noLogin(Model model){

        model.addAttribute("backgroundPic", "/images/restaurant.jpg");
        model.addAttribute("errorCode", null);
        return "error";
    }

}
