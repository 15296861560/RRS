package com.rrs.rrs.service;

import com.rrs.rrs.dto.BasketDetailDTO;
import com.rrs.rrs.dto.FoodDTO;
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

import java.util.ArrayList;
import java.util.List;

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
        Integer totalCount=basketDetailDTOS.size();//总数目
        pageDTO.setPageDTO(totalCount,page,size);
        pageDTO.setDataDTOS(basketDetailDTOS);
        return pageDTO;

    }


        //获取购物车细节并将其转换为DTO
    private List<BasketDetailDTO> getBasketDetail(Long userId) {
        Basket basket=basketMapper.findByUserId(userId);//获取该用户的购物车
        List<BasketDetail> basketDetails=basketDetailMapper.findByBasketId(basket.getBasketId());//获取该用户所有的购物车细节
        //将购物车细节里的食物信息读取出来,相同信息copy到BasketDetailDTO里，并对数据进行相应处理
        List<BasketDetailDTO> basketDetailDTOS=new ArrayList<>();
        for (BasketDetail basketDetail:basketDetails) {
            BasketDetailDTO basketDetailDTO=new BasketDetailDTO();
            Food food=foodMapper.findById(basketDetail.getFoodId());
            BeanUtils.copyProperties(basketDetail,basketDetailDTO);//把basketDetail的相同属性拷贝到BasketDetailDTO上面
            BeanUtils.copyProperties(food,basketDetailDTO);//把food的相同属性拷贝到BasketDetailDTO上面
            int qty=basketDetail.getQty();//数量
            basketDetailDTO.setSubtotal(food.getPrice()*qty);//小计
            basketDetailDTO.setType(FoodTypeEnum.valueOf(food.getType()).getMessage());//将类型转转换成中文
            basketDetailDTOS.add(basketDetailDTO);
        }

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
           order.setOrderTime("2020-02-20 16:17");
           order.setOrderStatus("APPLYING");
           List<BasketDetail> basketDetails=basketDetailMapper.findByBasketId(basketId);
           order.setContent(basketDetails.toString());
           orderMapper.createOrder(order);

           basketMapper.changeBasketStatus(basketId,"false");//更改购物车状态
           basketDetailMapper.deleteBasketDetailByBasketId(basketId);//所有该购物车的食物

           return true;
       }else {
           return false;

       }

    }
}
