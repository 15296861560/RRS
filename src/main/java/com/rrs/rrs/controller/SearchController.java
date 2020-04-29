package com.rrs.rrs.controller;

import com.rrs.rrs.dto.PageDTO;
import com.rrs.rrs.dto.UserDTO;
import com.rrs.rrs.enums.FoodTypeEnum;
import com.rrs.rrs.model.Seat;
import com.rrs.rrs.model.User;
import com.rrs.rrs.service.FoodService;
import com.rrs.rrs.service.OrderService;
import com.rrs.rrs.service.SeatService;
import com.rrs.rrs.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    //管理员查订单
    @PostMapping("/admin_search_order")
    public String searchOrder(Model model,
                             @RequestParam(value ="name",required = false,defaultValue = "全部")String name,
                             @RequestParam(value = "status",required = false)String status,
                             @RequestParam(value ="condition",required = false)String condition){


        PageDTO pageDTO=orderService.listSearch(1,5,name,status,condition);
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","order");
        return "manage";
    }

    //管理员根据位置查餐台
    @PostMapping("/admin_search_seat_location")
    public String searchSeatLocation(Model model,
                             @RequestParam(value ="location",required = false)String location,
                                     @RequestParam(name="page",defaultValue = "1")Integer page,
                                     @RequestParam(name="size",defaultValue = "5")Integer size){

        PageDTO pageDTO=seatService.findSeatByLocation(location,page,size);

        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","seat");
        return "manage";
    }
    //管理员根据状态查餐台
    @PostMapping("/admin_search_seat_status")
    public String searchSeatStatus(Model model,
                                     @RequestParam(value ="status",required = false)String status){


        PageDTO pageDTO=seatService.listSearchStatus(1,5,status);
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","seat");
        return "manage";
    }


    //管理员根据姓名查用户
    @PostMapping("/admin_search_user_name")
    public String searchUserByName(Model model,
                                     @RequestParam(value ="name",required = false)String name,
                                   @RequestParam(name="page",defaultValue = "1")Integer page,
                                    @RequestParam(name="size",defaultValue = "5")Integer size){
        PageDTO pageDTO=userService.findByName(name,page,size);
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","customer");
        return "manage";
    }

    //管理员根据联系号码查用户
    @PostMapping("/admin_search_user_phone")
    public String searchUserByPhone(Model model,
                                   @RequestParam(value ="phone",required = false)String phone,
                                   @RequestParam(name="page",defaultValue = "1")Integer page,
                                   @RequestParam(name="size",defaultValue = "5")Integer size){
        PageDTO pageDTO=userService.findByPhone(phone,page,size);
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","customer");
        return "manage";
    }

    //用户根据时间查询餐台
    @GetMapping("/findSeat")
    public String searchSeat(Model model,
                                   @RequestParam(value ="datetime",required = false)String datetime,
                             @RequestParam(name="page",defaultValue = "1")Integer page,
                             @RequestParam(name="size",defaultValue = "5")Integer size,
                             HttpServletResponse response){


        //如果未选择预约时间
        if (datetime==null||datetime.length()==0){
            model.addAttribute("tip","请先选择预约时间,再查询餐位！");
            model.addAttribute("src","/food");
            return "tip";
        }

        //用空格和-分隔datetime
        String[] dates = StringUtils.split(datetime, " |\\-");

        //将预约时间的信息存入在Cookie中，30分钟后过期
        Cookie orderTimeCookie = new Cookie("orderTime",dates[0]+"年"+dates[1]+"月"+dates[2]+"日"+dates[3]);
        orderTimeCookie.setMaxAge(60*30);
        response.addCookie(orderTimeCookie);
        PageDTO pageDTO=seatService.listSearchStatus(page,size,"空");
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("datetime",datetime);
        model.addAttribute("nav","food");
        return "seat-find";
    }


}
