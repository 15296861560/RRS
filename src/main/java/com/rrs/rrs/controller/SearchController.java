package com.rrs.rrs.controller;

import com.rrs.rrs.dto.PageDTO;
import com.rrs.rrs.service.FoodService;
import com.rrs.rrs.service.OrderService;
import com.rrs.rrs.service.SeatService;
import com.rrs.rrs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {
    @Autowired
    OrderService orderService;
    @Autowired
    FoodService foodService;
    @Autowired
    SeatService seatService;
    @Autowired
    UserService userService;

    @PostMapping("/admin_search_food")
    public String searchFood(Model model,
                           @RequestParam(value ="name",required = false,defaultValue = "全部")String name,
                           @RequestParam(value = "status",required = false)String status,
                           @RequestParam(value ="type",required = false)String type){


        PageDTO pageDTO=foodService.listSearch(1,5,name,status,type);
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","menu");
        return "manage";
    }
}
