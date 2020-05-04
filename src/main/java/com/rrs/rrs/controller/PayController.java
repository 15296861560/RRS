package com.rrs.rrs.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.rrs.rrs.config.AlipayConfig;
import com.rrs.rrs.dto.AlipayVo;
import com.rrs.rrs.model.Basket;
import com.rrs.rrs.model.Order;
import com.rrs.rrs.model.User;
import com.rrs.rrs.service.BasketService;
import com.rrs.rrs.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

import static com.rrs.rrs.config.AlipayConfig.app_id;
import static com.rrs.rrs.config.AlipayConfig.notify_url;

@Controller
public class PayController {

    @Autowired
    private OrderService orderService;
    @Autowired
    BasketService basketService;


    @GetMapping("/toPay")
    @ResponseBody
    public String toPay(HttpServletRequest request) throws Exception {

        //获取当前登录用户信息
        User user=(User)request.getSession().getAttribute("user");//从session中获取登录用户信息

        //获取已选菜单信息
        Order order=orderService.findApplying(user);
        //完善支付信息
        String result = Paying(order);

        return result;
    }

    @GetMapping("/toPayByBasketId")
    @ResponseBody
    public String toPayByBasketId(HttpServletRequest request,@RequestParam(name="basketId")Long basketId) throws Exception {


        //获取已选菜单信息
        Order order=orderService.findOrderByBasketId(basketId);
        String result = Paying(order);

        return result;
    }

    private String Paying(Order order) {
        //完善支付信息
        AlipayVo vo = new AlipayVo();
        vo.setOut_trade_no(order.getOrderId().toString());
        vo.setTotal_amount(order.getAmount().toString());
        vo.setSubject(order.getContent());
        //这个是固定的，沙箱默认就用这个参数
        vo.setProduct_code("FAST_INSTANT_TRADE_PAY");
        String json = JSON.toJSONString(vo);
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, app_id,
                AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
        // 设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
        alipayRequest.setBizContent(json);
        String result = null;
        try {
            result = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (Exception e) {
        }
        return result;
    }

    @GetMapping("/returnUrl")
    public String returnUrl(HttpServletRequest request, Model model) {

        Map<String, String[]> parameterMap = request.getParameterMap();
        //获取订单号
        String[] out_trade_nos = parameterMap.get("out_trade_no");
        Long orderId = Long.parseLong(out_trade_nos[0]);
        //将订单状态修改为预订成功的状态
        orderService.orderApplyOK(orderId);
        //获取订单信息
        Order order=orderService.findOrderById(orderId);
        model.addAttribute("tip","支付成功！请于"+order.getOrderTime()+"在"+order.getSeatId()+"号位置就餐");
        model.addAttribute("src","/food");
        return "tip";
    }

    @GetMapping("/notifyUrl")
    public String notifyUrl(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (String s : parameterMap.keySet()) {
            String[] strings = parameterMap.get(s);
            for (int i = 0; i < strings.length; i++) {
                System.out.println(s + ":" + strings[i]);
            }
        }
        return "redirect:/index";
    }
}

