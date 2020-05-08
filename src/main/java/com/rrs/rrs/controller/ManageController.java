package com.rrs.rrs.controller;


import com.alibaba.fastjson.JSONObject;
import com.rrs.rrs.dto.PageDTO;
import com.rrs.rrs.dto.ResultDTO;
import com.rrs.rrs.enums.FoodTypeEnum;
import com.rrs.rrs.enums.OrderStatusEnum;
import com.rrs.rrs.exception.CustomizeErrorCode;
import com.rrs.rrs.model.Admin;
import com.rrs.rrs.model.User;
import com.rrs.rrs.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    AdminService adminService;



    //订单管理
    @GetMapping("/manage")
    public String manage(Model model,
                         @RequestParam(name="page",defaultValue = "1")Integer page,//通过@RequestParam注解获取名字为page的参数默认值为1类型为Integer
                         @RequestParam(name="size",defaultValue = "5")Integer size,
                         HttpServletRequest httpServletRequest){

        PageDTO pageDTO=orderService.list(page,size);
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","order");

        return "manage";
    }

    //对订单进行管理
    @GetMapping("/manage/order/{action}")
    public String orderAction(Model model,
                             HttpServletRequest request,
                             @RequestParam(name="orderId")Long orderId,
                             @PathVariable(name = "action")String action){

        if (action.equals("delete")) orderService.deleteOrder(orderId);//删除指定订单
        if (action.equals("agree"))orderService.orderApplyOK(orderId);//将订单状态变为预订成功
        if (action.equals("finish"))orderService.orderFinish(orderId);//将订单状态变为已完成

        PageDTO pageDTO=orderService.list(1,5);
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
        PageDTO pageDTO=foodService.list(page,size);

        model.addAttribute("foodTypeS", FoodTypeEnum.values());
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
        model.addAttribute("foodTypeS", FoodTypeEnum.values());
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","menu");

        return "manage";

    }


    //餐台管理
    @GetMapping("/manage/seat")
    public String manage_seat(Model model,
                              @RequestParam(name="page",defaultValue = "1")Integer page,
                              @RequestParam(name="size",defaultValue = "5")Integer size,
                              HttpServletRequest httpServletRequest){

        PageDTO pageDTO=seatService.list(page,size);
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","seat");

        return "manage";
    }


    //对餐台进行相应操作
    @GetMapping("/manage/seat/{action}")
    public String seatAction(Model model,
                         HttpServletRequest request,
                         @RequestParam(name="seatId")Integer seatId,
                         @PathVariable(name = "action")String action){

        if (action.equals("delete")) seatService.deleteSeat(seatId);//删除指定餐台

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

        PageDTO pageDTO=userService.list(page,size);
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","customer");

        return "manage";
    }

    //帮助用户重置密码
    @GetMapping("/manage/customer/reset")
    public String customerAction(Model model,
                             HttpServletRequest request,
                             @RequestParam(name="userId")Long userId){

            model.addAttribute("nextUrl","/profile/changePassword");
            User user=userService.findById(userId);
            model.addAttribute("user",user);
            return "confirmPhone";

    }

    //删除用户
    @ResponseBody
    @RequestMapping(value = "/manage/customer/delete",method = RequestMethod.POST)
    public Object deleteUser(@RequestBody JSONObject dataJson,
                           HttpServletRequest request){
        String userId=dataJson.getString("userId");
        Admin admin= (Admin) request.getSession().getAttribute("admin");
        if (admin.getLevel()<8)//权限小于8级提示权限不足不能删除用户
        {
            return  ResultDTO.errorOf(CustomizeErrorCode.NEED_MORE_LEVEL);
        }else {
            return userService.deleteById(Long.parseLong(userId));
        }
    }


    //管理员管理
    @GetMapping("/manage/admin")
    public String manage_admin(Model model,
                                  @RequestParam(name="page",defaultValue = "1")Integer page,
                                  @RequestParam(name="size",defaultValue = "5")Integer size,
                                  HttpServletRequest httpServletRequest){

        PageDTO pageDTO=adminService.list(page,size);
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","admin");

        return "manage";
    }

    //高级管理员对管理员进行相应操作
    @GetMapping("/manage/admin/{action}")
    public String adminAction(Model model,
                                 HttpServletRequest request,
                                 @RequestParam(name="adminId")String adminId,
                                 @PathVariable(name = "action")String action){

        if (action.equals("delete")) adminService.deleteById(adminId);//删除指定管理员
        if (action.equals("up"))adminService.levelUp(adminId);//提升权限
        if (action.equals("down"))adminService.levelDown(adminId);//提升权限

        PageDTO pageDTO=adminService.list(1,5);
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","admin");
        return "manage";

    }


    //添加新管理员
    @ResponseBody
    @RequestMapping(value = "/manage/addAdmin",method = RequestMethod.POST)
    public Object addAdmin(@RequestBody JSONObject dataJson,
                         HttpServletRequest request){
        String adminId=dataJson.getString("adminId");
        String adminName=dataJson.getString("adminName");
        String phone=dataJson.getString("phone");
        return adminService.addAmin(adminId,adminName,phone);
    }


}
