package com.rrs.rrs.controller;

import com.alibaba.fastjson.JSON;
import com.rrs.rrs.service.BasketService;
import com.rrs.rrs.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    //已选菜品
    @GetMapping("/analysis/food")
    public String basket(Model model,
                         HttpServletRequest httpServletRequest){

        List rankData=basketService.getFoodRankData(10);
        List foodIdList= (List) rankData.get(0);
        List qtyList=(List) rankData.get(1);


        String foodIdListData=JSON.toJSONString(foodIdList);
        String qtyListData=JSON.toJSONString(qtyList);


        model.addAttribute("foodIdListData",foodIdListData);
        model.addAttribute("qtyListData",qtyListData);

        return "analysis";
    }
}
