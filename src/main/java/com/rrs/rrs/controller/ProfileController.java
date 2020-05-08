package com.rrs.rrs.controller;

import com.alibaba.fastjson.JSONObject;
import com.rrs.rrs.dto.*;
import com.rrs.rrs.enums.OrderStatusEnum;
import com.rrs.rrs.exception.CustomizeErrorCode;
import com.rrs.rrs.model.Basket;
import com.rrs.rrs.model.Food;
import com.rrs.rrs.model.Order;
import com.rrs.rrs.model.User;
import com.rrs.rrs.provider.ZhenziProvider;
import com.rrs.rrs.service.BasketService;
import com.rrs.rrs.service.OrderService;
import com.rrs.rrs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;
    @Autowired
    ZhenziProvider zhenziProvider;
    @Autowired
    BasketService basketService;


    @GetMapping("/profile")
    public String displayProfile(Model model,
                                 HttpServletRequest httpServletRequest){
        User user=(User)httpServletRequest.getSession().getAttribute("user");
        if (user==null){//未登录
            return "redirect:/noLogin";
        }
        user=userService.findById(user.getUserId());//更新user信息
        UserDTO userDTO=userService.userToDTO(user);
        userDTO.setHistory(orderService.getHistory(user.getUserId()));

        model.addAttribute("userDTO",userDTO);
        model.addAttribute("nav","profile");

        return "profile";
    }


    //下单历史
    @GetMapping("/profile/history")
    public String displayHistory(Model model,
                                 HttpServletRequest httpServletRequest,
                                 @RequestParam(name="page",defaultValue = "1")Integer page,
                                 @RequestParam(name="size",defaultValue = "5")Integer size){
        //获取登录用户
        User user=(User)httpServletRequest.getSession().getAttribute("user");
        Long userId=user.getUserId();
        //获取历史记录
        PageDTO pageDTO=orderService.getPageHistoryDTOS(page,size,userId);


        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("nav","profile");

        return "history";
    }

    //订单详情
    @GetMapping("/profile/history/details")
    public String displayDetails(Model model,
                                 HttpServletRequest httpServletRequest,
                                 @RequestParam(name="basketId",required = false)Long basketId){



        //获取订单详情
        List<BasketDetailDTO> basketDetailDTOS=basketService.getDetails(basketId);

        //获取订单状态
        Order order=orderService.findOrderByBasketId(basketId);
        if (order.getOrderStatus().equals(OrderStatusEnum.APPLYING.getStatus()))//如果订单状态为申请预订
        {
            model.addAttribute("basketId",basketId);
        }

        model.addAttribute("basketDetailDTOS",basketDetailDTOS);
        model.addAttribute("nav","profile");

        return "details";
    }

    //删除订单
    @GetMapping("/profile/deleteOrder")
    public String deleteFood(Model model,
                             @RequestParam(name="basketId",required = false)Long basketId){
        Order order=orderService.findOrderByBasketId(basketId);
        //删除订单
        orderService.deleteOrder(order.getOrderId());
        model.addAttribute("tip","删除订单成功");
        model.addAttribute("src","/profile/history");
        return "tip";
    }


    //撤销订单
    @GetMapping("/profile/apply/{OrderId}")
    public String apply(Model model,
                        @PathVariable(name = "OrderId")Long OrderId){

        orderService.setStatusToRequestReturn(OrderId);
        model.addAttribute("tip","撤销订单成功");
        model.addAttribute("src","/profile/history");
        return "tip";
    }


    //修改用户名
    @GetMapping("/profile/changeName")
    public String changeName(Model model,
                               HttpServletRequest request){
        return "changeName";
    }

    //去修改登录密码页面
    @GetMapping("/profile/changePassword")
    public String toChangePassword(Model model,
                                   @RequestParam(name="phone",required = false)String phone,
                               HttpServletRequest request){

        model.addAttribute("phone",phone);
        return "changePassword";
    }

    //修改登录密码
    @ResponseBody
    @RequestMapping(value ="/profile/changePassword",method = RequestMethod.POST)
    public Object changePassword(@RequestBody JSONObject dataJson,
                                 HttpServletRequest request){
        String password=dataJson.getString("password");
        String phone=dataJson.getString("phone");
        User user=userService.findByPhone(phone);
        return userService.changePassword(password,user);
    }

    //验证手机号码
    @GetMapping("/profile/confirmPhone/{action}")
    public String confirmPhone(Model model,
                               @PathVariable(name = "action")String action,
                               HttpServletRequest request){
        User user=(User)request.getSession().getAttribute("user");
        if (action.equals("changePhone")){//为了修改手机而验证号码
            model.addAttribute("nextUrl","/profile/changePhone");
        }else if (action.equals("changePassword")){//为了修改密码而验证号码
            model.addAttribute("nextUrl","/profile/changePassword");
        }
        model.addAttribute("user",user);
        return "confirmPhone";
    }

    //修改手机号码
    @GetMapping("/profile/changePhone")
    public String changePhone(Model model,
                               HttpServletRequest request){
        return "changePhone";
    }





    //验证验证码
    @ResponseBody
    @RequestMapping(value = "/profile/phone/verify",method = RequestMethod.POST)
    public Object verify(@RequestBody JSONObject dataJson,
                       HttpServletRequest request){
        String phone=dataJson.getString("phone");
        String verifyCode=dataJson.getString("verifyCode");
        return userService.verify(request, phone, verifyCode);
    }




}
