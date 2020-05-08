package com.rrs.rrs.controller;


import com.rrs.rrs.dto.FileDTO;
import com.rrs.rrs.dto.PageDTO;
import com.rrs.rrs.enums.FoodTypeEnum;
import com.rrs.rrs.exception.CustomizeErrorCode;
import com.rrs.rrs.model.Seat;
import com.rrs.rrs.provider.UCloudProvider;
import com.rrs.rrs.service.FoodService;
import com.rrs.rrs.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class UploadController {
    @Autowired
    FoodService foodService;
    @Autowired
    SeatService seatService;
    @Autowired
    private UCloudProvider uCloudProvider;


    @PostMapping("/uploadFood")
    public String uploadFood(Model model,
                           @RequestParam(value ="food_name",required = false)String food_name,
                           @RequestParam(value ="food_type",required = false)String food_type,
                           @RequestParam(value = "food_status",required = false)String food_status,
                           @RequestParam(value ="price",required = false)Double price,
                           @RequestParam(value ="food_pic",required = false) MultipartFile food_pic){


        //调用上传函数，并将上传结果存入fileDTO中
        FileDTO fileDTO=uploadFile(food_pic);


        //上传图片出错
        if (fileDTO.getSuccess()==0){
            model.addAttribute("errorMessage", CustomizeErrorCode.FOOD_UPLOAD_FAIL.getMessage());
            model.addAttribute("errorCode", CustomizeErrorCode.FOOD_UPLOAD_FAIL.getCode());
            return "error";
        }


        String foodType=null;
        for(FoodTypeEnum foodTypeEnum:FoodTypeEnum.values()){
            if (food_type.equals(foodTypeEnum.getMessage()))
                foodType=foodTypeEnum.getType();
        }

            foodService.createFood(food_name,foodType,price,food_status,fileDTO.getUrl());

        model.addAttribute("tip",food_type+"类型的菜品"+food_name+"上传成功");
        model.addAttribute("src","/manage/menu");
        return "tip";
    }

    //将图片存储到UCloud上的对象存储里
    public  FileDTO uploadFile(MultipartFile file){
        try {
            String fileName = uCloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            FileDTO fileDTO =new FileDTO();
            fileDTO.setSuccess(1);
            fileDTO.setUrl(fileName);
            return fileDTO;
        } catch (Exception e) {
            e.printStackTrace();
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(0);
            fileDTO.setMessage("上传失败");
            return fileDTO;
        }


    }


    @PostMapping("/uploadSeat")
    public String uploadSeat(Model model,
                             @RequestParam(value ="row",required = false)String row,
                             @RequestParam(value ="column",required = false)String column,
                             @RequestParam(value ="seat_pic",required = false) MultipartFile seat_pic){

        //调用上传函数，并将上传结果存入fileDTO中
        FileDTO fileDTO=uploadFile(seat_pic);


        //上传图片出错
        if (fileDTO.getSuccess()==0){
            model.addAttribute("errorMessage", CustomizeErrorCode.SEAT_UPLOAD_FAIL.getMessage());
            model.addAttribute("errorCode", CustomizeErrorCode.SEAT_UPLOAD_FAIL.getCode());
            return "error";
        }



        String location=row+"排"+column+"列";
        Seat seat=seatService.findSeatByLocation(location);
        if (seat==null)//如果这个位置的餐台不存在才能创建餐台
        {
            seatService.createSeat(location,fileDTO.getUrl());//创建某个位置的餐台
        }else {
            //上传餐台出错
            model.addAttribute("errorMessage", CustomizeErrorCode.SEAT_DUPLICATE_UPLOAD.getMessage());
            model.addAttribute("errorCode", CustomizeErrorCode.SEAT_DUPLICATE_UPLOAD.getCode());
            return "error";
        }

        model.addAttribute("tip","位置为"+location+"的餐台上传成功");
        model.addAttribute("src","/manage/seat");
        return "tip";

    }


    }
