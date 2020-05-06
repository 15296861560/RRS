package com.rrs.rrs.controller;


import com.alibaba.fastjson.JSONObject;
import com.rrs.rrs.dto.FoodDTO;
import com.rrs.rrs.dto.PageDTO;
import com.rrs.rrs.dto.ResultDTO;
import com.rrs.rrs.enums.FoodStatusEnum;
import com.rrs.rrs.enums.FoodTypeEnum;
import com.rrs.rrs.exception.CustomizeErrorCode;
import com.rrs.rrs.model.Admin;
import com.rrs.rrs.model.Food;
import com.rrs.rrs.model.User;
import com.rrs.rrs.service.BasketService;
import com.rrs.rrs.service.FoodService;
import com.rrs.rrs.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Controller
public class FoodController {

    @Autowired
    FoodService foodService;
    @Autowired
    BasketService basketService;
    @Autowired
    UserService userService;


    @GetMapping("/food")
    public String food(Model model,
                       @RequestParam(name="page",defaultValue = "1")Integer page,
                       @RequestParam(name="size",defaultValue = "9")Integer size,
                       @RequestParam(name="search",required = false)String search,//查询内容
                       @RequestParam(name="attribute",defaultValue = "name")String attribute,
                       HttpServletRequest request){
        //获取登录用户
        User user=(User)request.getSession().getAttribute("user");

        FoodPage(model, page, size, search, attribute,user);
        return "food";
    }

    //传统下单
    @GetMapping("/manage/tradition")
    public String tradition(Model model,
                       @RequestParam(name="page",defaultValue = "1")Integer page,
                       @RequestParam(name="size",defaultValue = "9")Integer size,
                       @RequestParam(name="search",required = false)String search,//查询内容
                       @RequestParam(name="attribute",defaultValue = "name")String attribute,
                            HttpServletRequest request){

        Admin admin= (Admin) request.getSession().getAttribute("admin");
        User user=userService.findByPhone(admin.getPhone());
        request.getSession().setAttribute("user",user);

        FoodPage(model, page, size, search, attribute,user);
        return "food";
    }

    public void FoodPage(Model model, @RequestParam(name = "page", defaultValue = "1") Integer page, @RequestParam(name = "size", defaultValue = "9") Integer size, @RequestParam(name = "search", required = false) String search, @RequestParam(name = "attribute", defaultValue = "name") String attribute,User user) {
        PageDTO pageDTO = new PageDTO();
        if ("name".equals(attribute) && (search == null||search=="")) pageDTO = foodService.listByStatus(page, size, "GOOD");//默认情况下
        else if ("name".equals(attribute)) pageDTO = foodService.listByName(page, size, search);
        else if ("type".equals(attribute)) pageDTO = foodService.listByType(page, size, search);

        //将食物类别再分类
        List list = FoodTypeEnum.valueOf("E").listByClassify();

        //获取销售为前5的菜品
        List foodList=basketService.getPopularFood(5);
        model.addAttribute("popularFood",foodList);


        if (user!=null)
        {
            //获取用户可能喜欢的菜品
            List likeList=basketService.getLikeFood(user);
            model.addAttribute("likeFood",likeList);
        }




        model.addAttribute("pageDTO", pageDTO);
        model.addAttribute("foodTypeS", list);//食物所有类型的枚举
        model.addAttribute("attribute", attribute);//查询和显示方式，名字或类别
        model.addAttribute("search", search);
        model.addAttribute("nav", "food");

        //如果是以类别的方式显示则把具体类别传到前端
        if ("type".equals(attribute)) model.addAttribute("classic", FoodTypeEnum.valueOf(search).getMessage());
    }


    //单个菜品页面
    @GetMapping("/food/{foodId}")
    public String foodDisplay(Model model,
                              @PathVariable(name = "foodId")Long foodId){
        Food food=foodService.findFoodById(foodId);
        FoodDTO foodDTO=foodService.getFoodDTO(food);
        model.addAttribute("foodDTO",foodDTO);
        model.addAttribute("nav", "food");
        return "singleFood";
    }


    @ResponseBody
    @RequestMapping(value = "/toOrder",method = RequestMethod.POST)
    public Object toOrder(@RequestBody JSONObject dataJson,
                         HttpServletRequest request){
        Long foodId=Long.parseLong(dataJson.getString("foodId"));//从json中获取食物id并将其转为Long类型
        Integer number=Integer.parseInt(dataJson.getString("number"));//从json中获取数量并将其转换为Integ类型
        User user=(User)request.getSession().getAttribute("user");//从session中获取登录用户信息

        //加入订单
        Long userId=user.getUserId();
        boolean flag=basketService.toOrder(foodId,number,userId);

        if (flag){
//            加入订单成功
            return ResultDTO.okOf();
        }
        else return ResultDTO.errorOf(CustomizeErrorCode.FAIL_TO_ORDER);
    }


}
