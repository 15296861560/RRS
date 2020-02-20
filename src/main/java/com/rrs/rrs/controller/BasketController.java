package com.rrs.rrs.controller;

import com.alibaba.fastjson.JSONObject;
import com.rrs.rrs.dto.BasketDetailDTO;
import com.rrs.rrs.dto.PageDTO;
import com.rrs.rrs.dto.ResultDTO;
import com.rrs.rrs.exception.CustomizeErrorCode;
import com.rrs.rrs.model.User;
import com.rrs.rrs.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class BasketController {

    @Autowired
    BasketService basketService;

    //已选菜品
    @GetMapping("/profile/basket")
    public String basket(Model model,
                         HttpServletRequest httpServletRequest){
        User user=(User)httpServletRequest.getSession().getAttribute("user");
        if (user==null){//未登录
            return "redirect:/noLogin";
        }

        PageDTO<BasketDetailDTO> pageDTO=basketService.listBasketDetail(1,9,user.getUserId());//获取购物车细节信息
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("nav","profile");
        return "shopcart";
    }


    //下单结算
    @ResponseBody
    @RequestMapping(value = "/Settle",method = RequestMethod.POST)
    public Object settle(@RequestBody JSONObject dataJson,
                          HttpServletRequest request){
        //从json中获取信息并将其转为对应类型
        Integer seatId=Integer.parseInt(dataJson.getString("seatId"));
        String orderTime=dataJson.getString("orderTime");

        User user=(User)request.getSession().getAttribute("user");//从session中获取登录用户信息

        Long userId=user.getUserId();
        boolean flag=basketService.settle(userId,orderTime,seatId);

        if (flag){
//            加入订单成功
            return ResultDTO.okOf();
        }
        else return ResultDTO.errorOf(CustomizeErrorCode.FAIL_TO_ORDER);
    }

}
