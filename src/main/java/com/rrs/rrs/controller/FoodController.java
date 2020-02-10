package com.rrs.rrs.controller;


import com.rrs.rrs.dto.FoodDTO;
import com.rrs.rrs.dto.PageDTO;
import com.rrs.rrs.enums.FoodStatusEnum;
import com.rrs.rrs.enums.FoodTypeEnum;
import com.rrs.rrs.model.Food;
import com.rrs.rrs.service.FoodService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class FoodController {

    @Autowired
    FoodService foodService;


    @GetMapping("/food")
    public String food(Model model,
                       @RequestParam(name="page",defaultValue = "1")Integer page,
                       @RequestParam(name="size",defaultValue = "9")Integer size,
                       @RequestParam(name="search",required = false)String search,//查询内容
                       @RequestParam(name="attribute",defaultValue = "name")String attribute){

        PageDTO pageDTO=new PageDTO();
        if ("name".equals(attribute)&&search==null)pageDTO=foodService.listByStatus(page,size,"GOOD");//默认情况下
        else if ("name".equals(attribute))pageDTO=foodService.listByName(page,size,search);
        else if ("type".equals(attribute))pageDTO=foodService.listByType(page,size,search);
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("foodTypeS", FoodTypeEnum.values());//食物所有类型的枚举
        model.addAttribute("attribute", attribute);//查询和显示方式，名字或类别
        model.addAttribute("search", search);
        //如果是以类别的方式显示则把具体类别传到前端
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


}
