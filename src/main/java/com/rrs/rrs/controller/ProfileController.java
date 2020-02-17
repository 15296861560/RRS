package com.rrs.rrs.controller;

import com.alibaba.fastjson.JSONObject;
import com.rrs.rrs.dto.BasketDetailDTO;
import com.rrs.rrs.dto.PageDTO;
import com.rrs.rrs.dto.ResultDTO;
import com.rrs.rrs.dto.UserDTO;
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

        model.addAttribute("userDTO",userDTO);
        model.addAttribute("nav","profile");

        return "profile";
    }

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

//    @GetMapping("/profile/history")
//    public String displayHistory(Model model,
//                                 HttpServletRequest httpServletRequest){
//        User user=(User)httpServletRequest.getSession().getAttribute("user");
//        Long userId=user.getUserId();
//        List<Order> Orders=OrderService.listByUserId(userId);
//        List<HistoryDTO> historyDTOS=new ArrayList();
//        for (Order Order:Orders){//循环将Order转换为historyDTO并将其加入列表中
//            HistoryDTO historyDTO = new HistoryDTO();
//            historyDTO.setId(Order.getId());
//            historyDTO.setFoodId(Order.getFoodId());
//            Food food=OrderService.findFoodById(Order.getFoodId());
//            historyDTO.setFoodNumber(Food.getNumber());
//            historyDTO.setFoodName(Food.getName());
//            historyDTO.setFoodCover(Food.getCover());
//            historyDTO.setGmtCreate(Order.getGmtCreate());
//            historyDTO.setGmtModified(Order.getGmtModified());
//            for (OrderStatusEnum orderStatusEnum:OrderStatusEnum.values()) {
//                if(OrderStatusEnum.getStatus().equals(Order.getStatus()))
//                    historyDTO.setStatus(OrderStatusEnum.getMessage());
//            }
//            historyDTOS.add(historyDTO);
//        }
//
//
//        model.addAttribute("historyDTOS",historyDTOS);
//
//        return "history";
//    }

    //撤销订单
    @GetMapping("/profile/apply/{OrderId}")
    public String apply(Model model,
                        @PathVariable(name = "OrderId")Long OrderId){

        orderService.setStatusToRequestReturn(OrderId);
        model.addAttribute("tip","撤销订单成功");
        model.addAttribute("src","/profile/history");
        return "tip";
    }

    @GetMapping("/profile/phone")
    public String bindingPhone(Model model,
                               HttpServletRequest request){


        return "phone";
    }

    @ResponseBody//把页面转化成其它结构
    @RequestMapping(value = "/profile/phone",method = RequestMethod.POST)
    public Object post(@RequestBody String data,
                       HttpServletRequest request){
        JSONObject dataJson = JSONObject.parseObject(data);
        String phone=dataJson.getString("phone");
        try {
            if (zhenziProvider.sendVerifyCode(request,phone))
                return ResultDTO.okOf();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultDTO.errorOf(CustomizeErrorCode.VERIFYCODE_SEND_FAIL);
    }

    @ResponseBody
    @RequestMapping(value = "/profile/phone/verify",method = RequestMethod.POST)
    public Object verify(@RequestBody JSONObject dataJson,
                       HttpServletRequest request){
        String phone=dataJson.getString("phone");
        String verifyCode=dataJson.getString("verifyCode");
        //获取存在session的验证信息
        JSONObject verify=(JSONObject)request.getSession().getAttribute("verify");
        String phone2=verify.getString("phone");
        String verifyCode2=verify.getString("verifyCode");
        //进行验证
        if (phone.equals(phone2)&&verifyCode.equals(verifyCode2)){
//            验证成功绑定手机号
            User userSession=(User)request.getSession().getAttribute("user");
            User user=userService.findById(userSession.getUserId());
//            userService.bindingPhone(user,phone);
            return ResultDTO.okOf();
        }
        else return ResultDTO.errorOf(CustomizeErrorCode.VERIFYCODE_VERIFY_FAIL);
    }



}
