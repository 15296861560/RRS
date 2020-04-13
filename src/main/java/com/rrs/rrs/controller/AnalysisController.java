package com.rrs.rrs.controller;

import com.rrs.rrs.service.BasketService;
import com.rrs.rrs.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

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

        ArrayList foodRankData=basketService.getFoodRankData(10);

        return "analysis";
    }
}
