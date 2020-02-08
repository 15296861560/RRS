package com.rrs.rrs.controller;


import com.rrs.rrs.dto.PageDTO;
import com.rrs.rrs.model.Admin;
import com.rrs.rrs.service.FoodService;
import com.rrs.rrs.service.OrderService;
import com.rrs.rrs.service.SeatService;
import com.rrs.rrs.service.UserService;
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
    @Autowired
    SeatService seatService;
    @Autowired
    UserService userService;



    //订单管理
    @GetMapping("/manage")
    public String manage(Model model,
                         @RequestParam(name="page",defaultValue = "1")Integer page,//通过@RequestParam注解获取名字为page的参数默认值为1类型为Integer
                         @RequestParam(name="size",defaultValue = "5")Integer size,
                         HttpServletRequest httpServletRequest){
        if (validate(httpServletRequest)) return "redirect:/noLogin";

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
        if (validate(httpServletRequest)) return "redirect:/noLogin";

        PageDTO pageDTO=foodService.list(page,size);
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","menu");

        return "manage";
    }


    //对食物进行相应操作
    @GetMapping("/manage/menu/{action}")
    public String deleteFood(Model model,
                        HttpServletRequest request,
                        @RequestParam(name="foodId")Long foodId,
                             @PathVariable(name = "action")String action){

        if (action.equals("delete")) foodService.deleteFood(foodId);//删除指定食物
        if (action.equals("toUp"))foodService.changeFoodStatus(foodId,"GOOD");//改变食物状态为上架
        if (action.equals("toDown"))foodService.changeFoodStatus(foodId,"STOCKING");//改变食物状态为下架

        PageDTO pageDTO=foodService.list(1,5);
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","menu");

        return "manage";

    }


    //座位管理
    @GetMapping("/manage/seat")
    public String manage_seat(Model model,
                              @RequestParam(name="page",defaultValue = "1")Integer page,
                              @RequestParam(name="size",defaultValue = "5")Integer size,
                              HttpServletRequest httpServletRequest){
        if (validate(httpServletRequest)) return "redirect:/noLogin";

        PageDTO pageDTO=seatService.list(page,size);
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","seat");

        return "manage";
    }


    //对座位进行相应操作
    @GetMapping("/manage/seat/{action}")
    public String seatAction(Model model,
                         HttpServletRequest request,
                         @RequestParam(name="seatId")Integer seatId,
                         @PathVariable(name = "action")String action){

        if (action.equals("delete")) seatService.deleteSeat(seatId);//删除指定座位
        if (action.equals("toEmpty"))seatService.changeSeatStatus(seatId,"空");//将座位状态变为空
        if (action.equals("toOrder"))seatService.changeSeatStatus(seatId,"有人");//将座位状态变为有人

        PageDTO pageDTO=seatService.list(1,5);
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","seat");

        return "manage";

    }




    //用户管理
    @GetMapping("/manage/customer")
    public String manage_customer(Model model,
                              @RequestParam(name="page",defaultValue = "1")Integer page,
                              @RequestParam(name="size",defaultValue = "5")Integer size,
                              HttpServletRequest httpServletRequest){
        if (validate(httpServletRequest)) return "redirect:/noLogin";

        PageDTO pageDTO=userService.list(page,size);
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","customer");

        return "manage";
    }




    //验证管理员是否已经登录
    private boolean validate(HttpServletRequest httpServletRequest) {
        Admin admin = (Admin) httpServletRequest.getSession().getAttribute("admin");
        if (admin == null) {//未登录
            return true;
        }
        return false;
    }


}
