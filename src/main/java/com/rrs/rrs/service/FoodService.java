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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FoodService {
    @Autowired
    private FoodMapper foodMapper;

    //寻找菜品
    public Food findFoodById(Long id){
        return foodMapper.findById(id);
    }


    //改变状态
    public void changeFoodStatus(Long foodId,String status){
        Food food=foodMapper.findById(foodId);
        food.setStatus(status);
        foodMapper.changeFoodStatus(food);
    }

    //添加新菜品
    public void createFood(String foodName, String type, Double price, String status,String foodUrl){
        Food food=new Food();
        food.setFoodName(foodName);
        food.setType(type);
        food.setPrice(price);
        food.setFoodUrl(foodUrl);
        food.setGmtCreate(System.currentTimeMillis());
        if (status.equals("上架"))status=FoodStatusEnum.GOOD.getStatus();
        if (status.equals("下架"))status=FoodStatusEnum.STOCKING.getStatus();
        food.setStatus(status);
        foodMapper.createFood(food);
    }

    //查询所有菜品并将其转为foodDTO存入list里
    public List<FoodDTO> listByFoodId(Integer status) {
        List<Food> foods=foodMapper.selectAll();//所有菜品
        return ToDTOS(foods);

    }


    //将所有菜品查询出来并进行分页处理
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

    //将所有上架菜品查询出来并进行分页处理
    public PageDTO listByStatus(Integer page, Integer size,String status,String sort,String condition) {

        PageDTO<FoodDTO> pageDTO=listSearch(page,size,"全部",status,"全部",sort,condition);

        return pageDTO;
    }

    //根据名称搜索上架菜品并进行分页处理
    public PageDTO listByName(Integer page, Integer size, String search,String sort,String condition) {
        PageDTO<FoodDTO> pageDTO=listSearch(page,size,search,"GOOD","全部",sort,condition);
        return pageDTO;
    }

    //根据类型搜索上架菜品并进行分页处理
    public PageDTO listByType(Integer page, Integer size, String search,String sort,String condition) {
        PageDTO<FoodDTO> pageDTO=listSearch(page,size,"全部","GOOD",search,sort,condition);
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


    //根据条件对菜品进行查询和分页
    public PageDTO listSearch(int page, int size,String name,String status,String type,String sort,String condition) {
        PageDTO<FoodDTO> pageDTO=new PageDTO();
        Integer offset=size*(page-1);//偏移量
        //构建查询条件
        DataQueryDTO dataQueryDTO =new DataQueryDTO();
        dataQueryDTO.setOffset(offset);
        dataQueryDTO.setSize(size);
        dataQueryDTO.setSort(sort);
        dataQueryDTO.setCondition(condition);
        if (name.equals("全部"))name="^";
        else name=stringToRegex(name);

        //对菜品状态进行转换
        if (status.equals("上架"))status=FoodStatusEnum.GOOD.getStatus();
        else if (status.equals("下架"))status=FoodStatusEnum.STOCKING.getStatus();
        else status="^";

        //对菜品类型进行转换
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

    //删除指定菜品
    public void deleteFood(Long foodId) {
        foodMapper.deleteFood(foodId);
    }


    //菜品类目统计
    public List getCategoryData() {
        //获取所有的菜品信息
        List<Food> analysisList=foodMapper.selectAll();

        HashMap<String,Integer> analysisMap=new HashMap();
        //将所有菜品类别当作主键加入analysisMap
        for (FoodTypeEnum type:FoodTypeEnum.values()) {
            analysisMap.put(type.getType(),0);
        }
        //统计各种菜品类别的数量
        String foodType;
        for (Food food:analysisList) {
            foodType=food.getType();
            if (analysisMap.containsKey(food.getType())){//如果analysisMap中有该类型的菜品，计数加1
                analysisMap.replace(foodType,analysisMap.get(foodType)+1);
            }else {//有什么意外情况都加到其它类里
                foodType="E";
                analysisMap.replace(foodType,analysisMap.get(foodType)+1);
            }
        }

        //对统计数据进行排序
        List<HashMap.Entry<String,Integer> > sortList = new ArrayList<HashMap.Entry<String,Integer> >(analysisMap.entrySet());
        Collections.sort(sortList, new Comparator<HashMap.Entry<String,Integer> >() {

            //降序排序
            @Override
            public int compare(HashMap.Entry<String,Integer> o1, HashMap.Entry<String,Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        //将类型和数量分离
        List typeList=new ArrayList();
        List qtyList=new ArrayList();
        for (HashMap.Entry<String,Integer> item:sortList) {
            String type=FoodTypeEnum.valueOf(item.getKey()).getMessage();//将类型转成中文
            typeList.add(type);
            qtyList.add(item.getValue());
        }
        List resultList=new ArrayList();
        resultList.add(typeList);
        resultList.add(qtyList);


        return resultList;
    }
}
