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


        //上传出错
        if (fileDTO.getSuccess()==0){
            model.addAttribute("errorMessage", CustomizeErrorCode.FOOD_UPLOAD_FAIL.getMessage());
            model.addAttribute("errorCode", CustomizeErrorCode.FOOD_UPLOAD_FAIL.getCode());
            return "error";
        }


        for(FoodTypeEnum foodTypeEnum:FoodTypeEnum.values()){
            if (food_type.equals(foodTypeEnum.getMessage()))
                food_type=foodTypeEnum.getType();
        }

            foodService.createFood(food_name,food_type,price,food_status,fileDTO.getUrl());

        model.addAttribute("fileDTO",fileDTO.getMessage());
        PageDTO pageDTO=foodService.list(1,5);
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","menu");

        return "manage";
    }

    //将图片存储到UCloud上的对象存储里
    public  FileDTO uploadFile(MultipartFile file){
        try {
            String fileName = uCloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            FileDTO fileDTO =new FileDTO();
            fileDTO.setSuccess(1);
            fileDTO.setUrl(fileName);
            return fileDTO;
        } catch (IOException e) {
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
                             @RequestParam(value ="column",required = false)String column){
        String location=row+"排"+column+"列";
        Seat seat=seatService.findSeatByLocation(location);
        if (seat==null)//如果这个位置的座位不存在才能创建座位
        {
            seatService.createSeat(location);//创建某个位置的座位
        }else {
            //上传座位出错
        }
        PageDTO pageDTO=seatService.list(1,5);
        model.addAttribute("pageDTO",pageDTO);
        model.addAttribute("section","seat");
        return "manage";

    }


    }
