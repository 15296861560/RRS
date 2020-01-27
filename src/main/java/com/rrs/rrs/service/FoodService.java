package com.rrs.rrs.service;

import com.rrs.rrs.dto.FoodDTO;
import com.rrs.rrs.dto.PageDTO;
import com.rrs.rrs.enums.FoodStatusEnum;
import com.rrs.rrs.enums.FoodTypeEnum;
import com.rrs.rrs.mapper.FoodMapper;
import com.rrs.rrs.model.Food;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodService {
    @Autowired
    private FoodMapper foodMapper;

    //寻找食物
    public Food findFoodById(Long id){
        return foodMapper.findById(id);
    }


    //改变状态
    public void changeFoodStatus(Long foodId,String status){
        Food food=foodMapper.findById(foodId);
        food.setStatus(status);
        foodMapper.changeFoodStatus(food);
    }

    //添加新食物
    public void createFood(String foodName, String type, Double price, String foodUrl){
        Food food=new Food();
        food.setFoodName(foodName);
        food.setType(type);
        food.setFoodUrl(foodUrl);
        food.setStatus("GOOD");
        foodMapper.createFood(food);
    }

    //查询所有食物并将其转为foodDTO存入list里
    public List<FoodDTO> listByFoodId(Integer status) {
        List<Food> foods=foodMapper.selectAll();//所有食物
        return ToDTOS(foods);

    }


    //分页数据
    public PageDTO list(String search, Integer page, Integer size, String attribute) {

        if (StringUtils.isNotBlank(search)){
            search = stringToRegex(search);

        }

        PageDTO<FoodDTO> pageDTO=new PageDTO();

//        //构建搜索条件
//        FoodDTO FoodDTO=new FoodDTO();
//        FoodDTO.setAttribute(attribute);
//        FoodDTO.setSearch(search);
//        FoodDTO.setTotalCount(getTotalCount(FoodDTO));
//        FoodDTO.setSize(size);
//        pageDTO.setPageDTO(FoodDTO.getTotalCount(),page,size);
//        Integer offset=size*(page-1);//偏移量
//        FoodDTO.setOffset(offset);
//        //进行搜索
//        List<Food> foods=getFoods(FoodDTO);
//        List<FoodDTO> foodDTOS=ToDTOS(foods);
//        pageDTO.setDataDTOS(foodDTOS);
        return pageDTO;
    }

    private String stringToRegex(String search) {
        //用空格分隔search
        String[] tags = StringUtils.split(search, " ");
        //用|把刚刚分隔的字符串重新拼接
        String regexTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        search=regexTag;
        return search;
    }

    // 将model转化为DTO
    private List<FoodDTO> ToDTOS(List<Food> foods){
        List<FoodDTO> foodDTOS=new ArrayList();
        for(Food food:foods){
            FoodDTO foodDTO=new FoodDTO();
            BeanUtils.copyProperties(food,foodDTO);//把food的所有相同属性拷贝到foodDTO上面
            foodDTO.setStatus(FoodStatusEnum.valueOf(food.getStatus()).getMessage());
            foodDTO.setType(FoodTypeEnum.valueOf(food.getType()).getMessage());
            foodDTOS.add(foodDTO);
        }
        return foodDTOS;
    }

    //获取总数
    private Integer getTotalCount(FoodDTO FoodDTO){
        Integer totalCount=0;
//        if (StringUtils.isBlank(FoodDTO.getSearch())) {
//            totalCount = foodMapper.foodCountAll();//书的总数
//        }else if ("name".equals(FoodDTO.getAttribute())){
//            totalCount = foodMapper.searchCountByName(FoodDTO.getSearch());//符合搜索条件的书的总数
//        }else {
//            totalCount = foodMapper.searchCountByType(FoodDTO.getSearch());//类别搜索
//        }
        return totalCount;
    }

    //返回分页后食物列表
    private List<Food> getFoods(FoodDTO FoodDTO){
        List<Food> foods=new ArrayList();
//        if (StringUtils.isBlank(FoodDTO.getSearch())) {
//            foods = foodMapper.listAll(FoodDTO.getOffset(),FoodDTO.getSize());//普通分页
//
//        }else if ("name".equals(FoodDTO.getAttribute())){
//            foods = foodMapper.listSearch(FoodDTO);//带搜索条件的分页
//        }else {
//            foods = foodMapper.listSearchByType(FoodDTO);
//        }
        return foods;
    }




}
