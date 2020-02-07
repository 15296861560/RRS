package com.rrs.rrs.controller;


import com.rrs.rrs.dto.FileDTO;
import com.rrs.rrs.dto.PageDTO;
import com.rrs.rrs.enums.FoodTypeEnum;
import com.rrs.rrs.exception.CustomizeErrorCode;
import com.rrs.rrs.provider.UCloudProvider;
import com.rrs.rrs.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class UploadController {
    @Autowired
    FoodService foodService;
    @Autowired
    private UCloudProvider uCloudProvider;


    @PostMapping("/upload")
    public String upload(Model model,
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

//        model.addAttribute("foodName",food_name);
//        model.addAttribute("src",src);图片延迟缓存，有时会加载不出，所以取消上传成功后显示图片
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

//    public static boolean uploadfile(MultipartFile file, String path, String fileName){
//
//        // 生成新的文件名
//        //String realPath = path + "/" + FileNameUtils.getFileName(fileName);
//
//        //使用原文件名
//        String realPath = path + "/" + fileName;
//
//        File dest = new File(realPath);
//
//        //判断文件父目录是否存在
//        if(!dest.getParentFile().exists()){
//            dest.getParentFile().mkdir();
//        }
//
//        try {
//            //保存文件
//            file.transferTo(dest);
//            return true;
//        } catch (IllegalStateException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return false;
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return false;
//        }
//
//    }


}
