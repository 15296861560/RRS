package com.rrs.rrs.service;

import com.rrs.rrs.dto.FoodDTO;
import com.rrs.rrs.dto.DataQueryDTO;
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
    public void createFood(String foodName, String type, Double price, String status,String foodUrl){
        Food food=new Food();
        food.setFoodName(foodName);
        food.setType(type);
        food.setPrice(price);
        food.setFoodUrl(foodUrl);
        if (status.equals("上架"))status=FoodStatusEnum.GOOD.getStatus();
        if (status.equals("下架"))status=FoodStatusEnum.STOCKING.getStatus();
        food.setStatus(status);
        foodMapper.createFood(food);
    }

    //查询所有食物并将其转为foodDTO存入list里
    public List<FoodDTO> listByFoodId(Integer status) {
        List<Food> foods=foodMapper.selectAll();//所有食物
        return ToDTOS(foods);

    }


    //将所有食物查询出来并进行分页处理
    public PageDTO list(Integer page, Integer size) {

        PageDTO<FoodDTO> pageDTO=new PageDTO();
        Integer totalCount;
        totalCount = foodMapper.foodCountAll();
        pageDTO.setPageDTO(totalCount,page,size);

        Integer offset=size*(page-1);//偏移量
        List<Food> foods=foodMapper.list(offset,size);//分页
        List<FoodDTO> foodDTOS=ToDTOS(foods);
        pageDTO.setDataDTOS(foodDTOS);
        return pageDTO;
    }

    //将所有上架食物查询出来并进行分页处理
    public PageDTO listByStatus(Integer page, Integer size,String status) {

        PageDTO<FoodDTO> pageDTO=listSearch(page,size,"全部",status,"全部");

        return pageDTO;
    }

    //根据名称搜索上架食物并进行分页处理
    public PageDTO listByName(Integer page, Integer size, String search) {
        PageDTO<FoodDTO> pageDTO=listSearch(page,size,search,"GOOD","全部");
        return pageDTO;
    }

    //根据类型搜索上架食物并进行分页处理
    public PageDTO listByType(Integer page, Integer size, String search) {
        PageDTO<FoodDTO> pageDTO=listSearch(page,size,"全部","GOOD",search);
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
            FoodDTO foodDTO = getFoodDTO(food);
            foodDTOS.add(foodDTO);
        }
        return foodDTOS;
    }

    public FoodDTO getFoodDTO(Food food) {
        FoodDTO foodDTO=new FoodDTO();
        BeanUtils.copyProperties(food,foodDTO);//把food的所有相同属性拷贝到foodDTO上面
        foodDTO.setStatus(FoodStatusEnum.valueOf(food.getStatus()).getMessage());
        foodDTO.setType(FoodTypeEnum.valueOf(food.getType()).getMessage());
        return foodDTO;
    }


    //根据条件对食物进行查询和分页
    public PageDTO listSearch(int page, int size,String name,String status,String type) {
        PageDTO<FoodDTO> pageDTO=new PageDTO();
        Integer offset=size*(page-1);//偏移量
        //构建查询条件
        DataQueryDTO dataQueryDTO =new DataQueryDTO();
        dataQueryDTO.setOffset(offset);
        dataQueryDTO.setSize(size);
        if (name.equals("全部"))name="^";
        else name=stringToRegex(name);

        //对食物状态进行转换
        if (status.equals("上架"))status=FoodStatusEnum.GOOD.getStatus();
        else if (status.equals("下架"))status=FoodStatusEnum.STOCKING.getStatus();
        else status="^";

        //对食物类型进行转换
        for (FoodTypeEnum footType:FoodTypeEnum.values()) {
            if (type.equals(footType.getMessage())){
                type=footType.getType();
                break;
            }
        }
        if (type.equals("全部"))type="^";
        dataQueryDTO.setName(name);
        dataQueryDTO.setStatus(status);
        dataQueryDTO.setType(type);

        List<Food> foods=foodMapper.listSearch(dataQueryDTO);//分页
        List<FoodDTO> foodDTOS=ToDTOS(foods);

        Integer totalCount;
        totalCount =foodMapper.listSearchCount(dataQueryDTO);//查搜索数目
        pageDTO.setPageDTO(totalCount,page,size);
        pageDTO.setDataDTOS(foodDTOS);
        return pageDTO;
    }

    //删除指定食物
    public void deleteFood(Long foodId) {
        foodMapper.deleteFood(foodId);
    }




}
