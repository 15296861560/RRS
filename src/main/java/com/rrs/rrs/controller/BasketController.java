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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        Integer seatId=null;
        String orderTime=null;
        //通过request获取Cookie
        Cookie[] cookies = request.getCookies();
        //从Cookie中获取预约座位和预约时间
        if (cookies!=null&&cookies.length!=0)//cookie不为null时
            for (Cookie cookie:cookies) {
                if (cookie.getName().equals("seatId")) {
                    seatId=Integer.parseInt(cookie.getValue());
                }
                if (cookie.getName().equals("orderTime")) {
                    orderTime=cookie.getValue();
                }
            }

            if (seatId==null||orderTime==null){//检查是否已经预约过座位
                return ResultDTO.errorOf(CustomizeErrorCode.FAIL_TO_ORDER);
            }

        User user=(User)request.getSession().getAttribute("user");//从session中获取登录用户信息
        Long userId=user.getUserId();
        boolean flag=basketService.settle(userId,orderTime,seatId);//进行结算创建订单

        if (flag){
//            加入订单成功
            return ResultDTO.okOf();
        }
        else return ResultDTO.errorOf(CustomizeErrorCode.FAIL_TO_ORDER);
    }

    //预约座位
    @ResponseBody
    @RequestMapping(value = "/toOrderSeat",method = RequestMethod.POST)
    public Object toOrderSeat(@RequestBody JSONObject dataJson,
                         HttpServletRequest request, HttpServletResponse response){
        //从json中获取信息并将其转为对应类型
        String seatId=dataJson.getString("seatId");
        String orderTime=dataJson.getString("orderTime");

        //将预约座位的信息存入在Cookie中，30分钟后过期
        Cookie seatIdCookie = new Cookie("seatId", seatId);
        seatIdCookie.setMaxAge(60*30);
        Cookie orderTimeCookie = new Cookie("orderTime",orderTime);
        orderTimeCookie.setMaxAge(60*30);
        response.addCookie(seatIdCookie);
        response.addCookie(orderTimeCookie);

            return ResultDTO.okOf();
    }

}
