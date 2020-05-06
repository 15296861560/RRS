package com.rrs.rrs.service;

import com.rrs.rrs.dto.BasketDetailDTO;
import com.rrs.rrs.dto.PageDTO;
import com.rrs.rrs.enums.FoodTypeEnum;
import com.rrs.rrs.mapper.BasketDetailMapper;
import com.rrs.rrs.mapper.BasketMapper;
import com.rrs.rrs.mapper.FoodMapper;
import com.rrs.rrs.mapper.OrderMapper;
import com.rrs.rrs.model.Basket;
import com.rrs.rrs.model.BasketDetail;
import com.rrs.rrs.model.Food;
import com.rrs.rrs.model.Order;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BasketService {
    @Autowired
    private BasketMapper basketMapper;
    @Autowired
    private BasketDetailMapper basketDetailMapper;
    @Autowired
    private FoodMapper foodMapper;
    @Autowired
    private OrderMapper orderMapper;

    public boolean toOrder(Long foodId, Integer number,Long userId) {

        Basket basket=basketMapper.findByUserId(userId);//根据用户查看他的购物车
        if (basket==null)//如果没有该购物车的话，为该用户创建一个新的购物车
        {
            Basket basket1=new Basket();
            basket1.setUserId(userId);
            basket1.setGmtCreate(System.currentTimeMillis());
            basket1.setGmtModified(System.currentTimeMillis());
            basket1.setBasketStatus("true");
            basket1.setPayment(0.0);
            basketMapper.createBasket(basket1);
            basket=basketMapper.findByUserId(userId);//重新获取basket
        }else if (basket.getBasketStatus().equals("false")){//购物车的状态为false
            basketMapper.changeBasketStatus(basket.getBasketId(),"true");//将购物车状态改为true
        }

        //想要新加入的细节
        BasketDetail basketDetail=new BasketDetail();
        basketDetail.setFoodId(foodId);
        basketDetail.setQty(number);
        basketDetail.setBasketId(basket.getBasketId());
        //将食物加入basketDetai表里
        List<BasketDetail> basketDetails=basketDetailMapper.findByBasketId(basket.getBasketId());
        if (basketDetails.isEmpty()){//如果该购物车的basketDetail没有数据的话直接加入新数据
            basketDetailMapper.createBasketDetail(basketDetail);
        }else {//如果有数据的话，检查新加入的是否已经在购物车里了有的话合并，没有的话直接加入
            boolean flag=true;
            Long temId=0l;
            for (BasketDetail bd:basketDetails) {
                if (bd.getFoodId().equals(foodId)){//如果有相同食物
                    flag=false;//flag改为为false，表示有相同食物
                    temId=bd.getBasketDetailId();//获取这个食物的basketDetailId
                }
            }
            if (flag){//没有相同食物直接加入
                basketDetailMapper.createBasketDetail(basketDetail);
            }else {//有相同食物进行合并
                BasketDetail newBasketDetail1=basketDetailMapper.findById(temId);
                int qty=newBasketDetail1.getQty()+number;//数量相加
                newBasketDetail1.setQty(qty);
                basketDetailMapper.changeBasketDetailQty(newBasketDetail1);
            }

        }
        //某种食物加入订单后
        Food food=foodMapper.findById(foodId);
        //修改支付金额
        double payment=basket.getPayment()+food.getPrice()*number;
        basket.setPayment(payment);
        //更新数据库中的购物车
        basketMapper.changePayment(basket);

        return true;


    }

    //对购物车细节进行分页处理
    public PageDTO listBasketDetail(int page, int size,Long userId) {
        List<BasketDetailDTO> basketDetailDTOS=getBasketDetail(userId);//获取购物车细节信息
        PageDTO<BasketDetailDTO> pageDTO=new PageDTO();

        Integer offset=size*(page-1);//偏移量
        //总数目
        Integer totalCount;
        if (basketDetailDTOS==null)totalCount=0;
        else totalCount=basketDetailDTOS.size();

        pageDTO.setPageDTO(totalCount,page,size);
        pageDTO.setDataDTOS(basketDetailDTOS);
        return pageDTO;

    }


        //获取购物车细节并将其转换为DTO
    private List<BasketDetailDTO> getBasketDetail(Long userId) {
        Basket basket=basketMapper.findByUserId(userId);//获取该用户的购物车
        //判断该用户是否创建过购物车
        if (basket==null)return null;
        List<BasketDetail> basketDetails=basketDetailMapper.findByBasketId(basket.getBasketId());//获取该用户所有的购物车细节
        //将购物车细节里的食物信息读取出来,相同信息copy到BasketDetailDTO里，并对数据进行相应处理
        List<BasketDetailDTO> basketDetailDTOS = getBasketDetailDTOS(basketDetails);

        return basketDetailDTOS;
    }

    //订单结算
    public boolean settle(Long userId,String orderTime,Integer seatId) {
       Basket basket= basketMapper.findByUserId(userId);
       Long basketId=basket.getBasketId();
       if (basket.getUserId().equals(userId)){//是本人进行结算则进行结算操作

           //创建订单
           Order order=new Order();
           order.setUserId(userId);
           order.setSeatId(seatId);
           order.setAmount(basket.getPayment());
           order.setOrderTime(orderTime);
           order.setOrderStatus("APPLYING");
           order.setBasketId(basketId);
           List<BasketDetail> basketDetails=basketDetailMapper.findByBasketId(basketId);
           //添加订单内容
           String content="";
           for (BasketDetail basketDetail:basketDetails) {
               Food food=foodMapper.findById(basketDetail.getFoodId());
               content=content+food.getFoodName()+basketDetail.getQty()+"份；";
           }
           order.setContent(content);

           orderMapper.createOrder(order);

           basketMapper.changeBasketStatus(basketId,"false");//更改购物车状态
           basketMapper.changeBasketGmtModified(basketId,System.currentTimeMillis());//更改购物车状态

           return true;
       }else {
           return false;

       }

    }


    //获取排名前l的最受欢迎菜品（名称和销售数量）
    public List getFoodRankData(int l) {
        List<Map.Entry<Long, Integer>> sortList = getFoodSellSortList();

        //获取排名前l条数据
        if (sortList.size() > l) {//判断list长度
            sortList = sortList.subList(0, l);//取前l条数据
        }

        //将foodId和数量分离
        List foodList=new ArrayList();
        List qtyList=new ArrayList();
        for (HashMap.Entry<Long,Integer> item:sortList) {
            Food food=foodMapper.findById(item.getKey());
            foodList.add(food.getFoodName());
            qtyList.add(item.getValue());
        }
        List resultList=new ArrayList();
        resultList.add(foodList);
        resultList.add(qtyList);
            return resultList;

    }

    //获取排名前l的最受欢迎菜品（food）
    public List getPopularFood(int l) {
        List<Map.Entry<Long, Integer>> sortList = getFoodSellSortList();

        //获取排名前l条数据
        if (sortList.size() > l) {//判断list长度
            sortList = sortList.subList(0, l);//取前l条数据
        }
        //将id转成food
        List foodList=new ArrayList();

        for (HashMap.Entry<Long,Integer> item:sortList) {
            Food food=foodMapper.findById(item.getKey());
            foodList.add(food);
        }

        return foodList;
    }

    //获取销售菜品的销售情况，按降序排列
    private List<Map.Entry<Long, Integer>> getFoodSellSortList() {
        //获取所有的购物车细节
        ArrayList<BasketDetail> basketDetailList = basketDetailMapper.getAllBasketDetail();
        //获取所有状态为true的购物车id
        ArrayList basketIdList = basketMapper.getAllBasketStatusTrue();

        //获取所有的已购买历史
        ArrayList<BasketDetail> analysisList = new ArrayList();
        for (BasketDetail basketDetail : basketDetailList) {
            if (!basketIdList.contains(basketDetail.getBasketId())) {
                analysisList.add(basketDetail);
            }
        }

        //统计各个菜品被购买的次数
        HashMap<Long, Integer> analysisMap = new HashMap();
        for (BasketDetail basketDetail : analysisList) {
            Long foodId = basketDetail.getFoodId();
            if (analysisMap.containsKey(foodId)) {//如果analysisMap中有该食物的id，计数加数量
                analysisMap.replace(foodId, analysisMap.get(foodId) + basketDetail.getQty());
            } else {//果analysisMap中没有该食物的id，以该id为键1为值加入analysisMap中
                analysisMap.put(foodId, 1);
            }
        }

        //对统计数据进行排序
        List<HashMap.Entry<Long, Integer>> sortList = new ArrayList<HashMap.Entry<Long, Integer>>(analysisMap.entrySet());
        Collections.sort(sortList, new Comparator<HashMap.Entry<Long, Integer>>() {

            //降序排序
            @Override
            public int compare(HashMap.Entry<Long, Integer> o1, HashMap.Entry<Long, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        return sortList;
    }

    //删除购物车上的某种食物
    public void deleteBasketDetail(Long basketDetailId) {
        basketDetailMapper.deleteBasketDetailById(basketDetailId);
    }

    //根据id查购物车
    public Basket findById(Long basketId){
        return basketMapper.findById(basketId);
    }

    public List<BasketDetailDTO> getDetails(Long basketId) {

        List<BasketDetail> basketDetails=basketDetailMapper.findByBasketId(basketId);//获取该用户所有的购物车细节
        //将购物车细节里的食物信息读取出来,相同信息copy到BasketDetailDTO里，并对数据进行相应处理
        List<BasketDetailDTO> basketDetailDTOS = getBasketDetailDTOS(basketDetails);

        return basketDetailDTOS;

    }

    private List<BasketDetailDTO> getBasketDetailDTOS(List<BasketDetail> basketDetails) {
        //将购物车细节里的食物信息读取出来,相同信息copy到BasketDetailDTO里，并对数据进行相应处理
        List<BasketDetailDTO> basketDetailDTOS = new ArrayList<>();
        for (BasketDetail basketDetail : basketDetails) {
            BasketDetailDTO basketDetailDTO = new BasketDetailDTO();
            Food food = foodMapper.findById(basketDetail.getFoodId());
            BeanUtils.copyProperties(basketDetail, basketDetailDTO);//把basketDetail的相同属性拷贝到BasketDetailDTO上面
            BeanUtils.copyProperties(food, basketDetailDTO);//把food的相同属性拷贝到BasketDetailDTO上面
            int qty = basketDetail.getQty();//数量
            basketDetailDTO.setSubtotal(food.getPrice() * qty);//小计
            basketDetailDTO.setType(FoodTypeEnum.valueOf(food.getType()).getMessage());//将类型转转换成中文
            basketDetailDTOS.add(basketDetailDTO);
        }
        return basketDetailDTOS;
    }
}
