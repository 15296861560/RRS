package com.rrs.rrs.controller;


import com.rrs.rrs.dto.PageDTO;
import com.rrs.rrs.model.Admin;
import com.rrs.rrs.service.FoodService;
import com.rrs.rrs.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ManageController {
    @Autowired
    OrderService orderService;
    @Autowired
    FoodService foodService;

    //订单管理
    @GetMapping("/manage")
    public String manage(Model model,
                         @RequestParam(name="page",defaultValue = "1")Integer page,//通过@RequestParam注解获取名字为page的参数默认值为1类型为Integer
                         @RequestParam(name="size",defaultValue = "5")Integer size,
                         HttpServletRequest httpServletRequest){
        Admin admin=(Admin)httpServletRequest.getSession().getAttribute("admin");
        if (admin==null){//未登录
            return "redirect:/noLogin";
        }

        PageDTO pageDTO=orderService.list(page,size);
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","order");

        return "manage";
    }

    //菜单管理
    @GetMapping("/manage/menu")
    public String manage_menu(Model model,
                         @RequestParam(name="page",defaultValue = "1")Integer page,
                         @RequestParam(name="size",defaultValue = "5")Integer size,
                         HttpServletRequest httpServletRequest){
        Admin admin=(Admin)httpServletRequest.getSession().getAttribute("admin");
        if (admin==null){//未登录
            return "redirect:/noLogin";
        }

        PageDTO pageDTO=foodService.list(page,size);
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","menu");

        return "manage";
    }







}
