package com.rrs.rrs.service;

import com.rrs.rrs.mapper.BasketDetailMapper;
import com.rrs.rrs.mapper.BasketMapper;
import com.rrs.rrs.model.Basket;
import com.rrs.rrs.model.BasketDetail;
import com.rrs.rrs.model.Food;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasketService {
    @Autowired
    private BasketMapper basketMapper;
    @Autowired
    private BasketDetailMapper basketDetailMapper;
    @Autowired
    private FoodService foodService;

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
        Food food=foodService.findFoodById(foodId);
        //修改支付金额
        double payment=basket.getPayment()+food.getPrice()*number;
        basket.setPayment(payment);
        //更新数据库中的购物车
        basketMapper.changePayment(basket);

        return true;


    }
}
