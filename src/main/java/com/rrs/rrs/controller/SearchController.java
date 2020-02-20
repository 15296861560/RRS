package com.rrs.rrs.controller;

import com.rrs.rrs.dto.PageDTO;
import com.rrs.rrs.enums.FoodTypeEnum;
import com.rrs.rrs.model.Seat;
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

    //管理员查食物
    @PostMapping("/admin_search_food")
    public String searchFood(Model model,
                           @RequestParam(value ="name",required = false,defaultValue = "全部")String name,
                           @RequestParam(value = "status",required = false)String status,
                           @RequestParam(value ="type",required = false)String type){


        PageDTO pageDTO=foodService.listSearch(1,5,name,status,type);
        model.addAttribute("foodTypeS", FoodTypeEnum.values());
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","menu");
        return "manage";
    }

    //管理员根据位置查座位
    @PostMapping("/admin_search_seat_location")
    public String searchSeatLocation(Model model,
                             @RequestParam(value ="location",required = false)String location){


        Seat seat=seatService.findSeatByLocation(location);
        model.addAttribute("pageDTO",seat);
        model.addAttribute("section","seat");
        return "manage";
    }
    //管理员根据状态查座位
    @PostMapping("/admin_search_seat_status")
    public String searchSeatStatus(Model model,
                                     @RequestParam(value ="status",required = false)String status){


        PageDTO pageDTO=seatService.listSearchStatus(1,5,status);
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","seat");
        return "manage";
    }


}
