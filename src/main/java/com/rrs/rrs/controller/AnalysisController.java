package com.rrs.rrs.controller;

import com.alibaba.fastjson.JSON;
import com.rrs.rrs.service.BasketService;
import com.rrs.rrs.service.FoodService;
import com.rrs.rrs.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class AnalysisController {
    @Autowired
    FoodService foodService;
    @Autowired
    BasketService basketService;
    @Autowired
    OrderService orderService;



    @GetMapping("/analysis/{action}")
    public String food(Model model,
                         HttpServletRequest httpServletRequest,
                       @PathVariable(name = "action")String action){

        List rankData=new ArrayList();
        if (action.equals("food")) {//最受欢迎菜品
            rankData=basketService.getFoodRankData(10);
            model.addAttribute("section","food");
        }
        if (action.equals("user")) {//点餐最多客户
            rankData=orderService.getUserRankData(10);
            model.addAttribute("section","user");
        }

        List nameList= (List) rankData.get(0);
        List qtyList=(List) rankData.get(1);


        String nameListData=JSON.toJSONString(nameList);
        String qtyListData=JSON.toJSONString(qtyList);


        model.addAttribute("nameListData",nameListData);
        model.addAttribute("qtyListData",qtyListData);

        return "analysis";
    }

}
