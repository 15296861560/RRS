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
        model.addAttribute("section","manage");

        return "manage";
    }

    @GetMapping("/manage/{action}/{orderId}")
    public String handleBorrow(@PathVariable(name = "action")String action,
                         @PathVariable(name = "orderId")Long orderId
    ){
        //处理订单
//        if ("agree".equals(action)) {
//
//            orderService.agree(orderId);
//
//        }
//        if ("reject".equals(action))
//            orderService.borrowFail(orderId);
        return "redirect:/manage";
    }




    @GetMapping("/checkFood")
    public String search(Model model,
                         @RequestParam(name="page",defaultValue = "1")Integer page,
                         @RequestParam(name="size",defaultValue = "9")Integer size,
                         @RequestParam(name="search",required = false)String search,
                         HttpServletRequest httpServletRequest){

        Admin admin=(Admin)httpServletRequest.getSession().getAttribute("admin");
        if (admin==null){//未登录
            return "redirect:/noLogin";
        }

        PageDTO pageDTO=foodService.list(search,page,size,"name");
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","checkBook");
        model.addAttribute("search",search);
        return "manage";
    }





}
