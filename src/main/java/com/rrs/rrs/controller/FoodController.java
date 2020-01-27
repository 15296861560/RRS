package com.rrs.rrs.controller;


import com.rrs.rrs.dto.FoodDTO;
import com.rrs.rrs.dto.PageDTO;
import com.rrs.rrs.enums.FoodStatusEnum;
import com.rrs.rrs.enums.FoodTypeEnum;
import com.rrs.rrs.model.Food;
import com.rrs.rrs.model.User;
import com.rrs.rrs.service.FoodService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class FoodController {

    @Autowired
    FoodService foodService;


    @GetMapping("/food")
    public String food(Model model,
                       @RequestParam(name="page",defaultValue = "1")Integer page,
                       @RequestParam(name="size",defaultValue = "9")Integer size,
                       @RequestParam(name="search",required = false)String search,
                       @RequestParam(name="attribute",defaultValue = "name")String attribute){

        PageDTO pageDTO=foodService.list(search,page,size,attribute);
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("classify", FoodTypeEnum.values());
        model.addAttribute("attribute", attribute);
        model.addAttribute("search", search);
        if ("type".equals(attribute)) model.addAttribute("classic", FoodTypeEnum.valueOf(search).getMessage());
        return "food";
    }

    @GetMapping("/food/{foodId}")
    public String foodDisplay(Model model,
                              @PathVariable(name = "foodId")Long foodId){
        Food food=foodService.findFoodById(foodId);
        FoodDTO foodDTO=new FoodDTO();
        BeanUtils.copyProperties(food,foodDTO);//把food的所有属性拷贝到foodDTO上面
        foodDTO.setStatus(FoodStatusEnum.valueOf(food.getStatus()).getMessage());
        foodDTO.setType(FoodTypeEnum.valueOf(food.getType()).getMessage());
        model.addAttribute("foodDTO",foodDTO);
        return "singleFood";
    }

    @GetMapping("/food/apply/{foodId}")
    public String apply(Model model,
                        HttpServletRequest request,
                        @PathVariable(name = "foodId")Long foodId){
        User user = (User) request.getSession().getAttribute("user");
        if (user==null){//未登录
            return "redirect:/noLogin";
        }
        //创建订单记录

        //将订单状态变为预约中
        model.addAttribute("tip","申请成功请等待餐厅接受预约");
        model.addAttribute("src","/food");
        return "tip";
    }
}
