package com.rrs.rrs.controller;

import com.alibaba.fastjson.JSONObject;
import com.rrs.rrs.dto.ResultDTO;
import com.rrs.rrs.dto.UserDTO;
import com.rrs.rrs.exception.CustomizeErrorCode;
import com.rrs.rrs.model.User;
import com.rrs.rrs.provider.ZhenziProvider;
import com.rrs.rrs.service.OrderService;
import com.rrs.rrs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;
    @Autowired
    ZhenziProvider zhenziProvider;


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


    //修改用户名
    @GetMapping("/profile/changeName")
    public String changeName(Model model,
                               HttpServletRequest request){
        return "changeName";
    }

    //修改登录密码
    @GetMapping("/profile/changePassword")
    public String changePassword(Model model,
                               HttpServletRequest request){
        return "changePassword";
    }

    //验证手机号码
    @GetMapping("/profile/confirmPhone")
    public String confirmPhone(Model model,
                               HttpServletRequest request){
        User user=(User)request.getSession().getAttribute("user");

        model.addAttribute("user",user);
        return "confirmPhone";
    }

    //修改手机号码
    @GetMapping("/profile/changePhone")
    public String changePhone(Model model,
                               HttpServletRequest request){
        return "changePhone";
    }

    //重新绑定手机号码
    @ResponseBody
    @RequestMapping(value = "/profile/phone/binding",method = RequestMethod.POST)
    public Object binding(@RequestBody JSONObject dataJson,
                         HttpServletRequest request){
        ResultDTO result=(ResultDTO)userService.getVerify(dataJson, request);
        if (result.getCode()==200){//验证成功进行绑定
            userService.bindingPhone(dataJson, request);
        }

        return result;
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
