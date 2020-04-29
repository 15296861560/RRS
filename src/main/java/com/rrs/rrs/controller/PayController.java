package com.rrs.rrs.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.rrs.rrs.config.AlipayConfig;
import com.rrs.rrs.dto.AlipayVo;
import com.rrs.rrs.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
//    @Autowired
//    private RedisServer redisServer;

    @GetMapping("/toPay")
    @ResponseBody
    public String toPay(@RequestParam(value = "sumMoney",required = false)Double sumMoney,
                        @RequestParam(value = "basketId",required = false)String basketId) throws Exception {
        AlipayVo vo = new AlipayVo();
        //这里模拟了一个订单的id
        vo.setOut_trade_no(UUID.randomUUID().toString().replace("-", ""));
        vo.setTotal_amount("1.11");
        vo.setSubject("测试付款商品A");
        //这个是固定的，沙箱默认就用这个参数
        vo.setProduct_code("FAST_INSTANT_TRADE_PAY");
        String json = JSON.toJSONString(vo);
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, app_id,
                AlipayConfig.merchant_private_key, "json",AlipayConfig.charset, AlipayConfig.alipay_public_key,AlipayConfig.sign_type);
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

    @RequestMapping("returnUrl")
    public String returnUrl(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String[] out_trade_nos = parameterMap.get("out_trade_no");
        String uuid = out_trade_nos[0];
//        orderService.updateOrderInfo(uuid);
        return "redirect:/food";
    }

    @RequestMapping("notifyUrl")
    public String notifyUrl(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (String s : parameterMap.keySet()) {
            String[] strings = parameterMap.get(s);
            for (int i = 0; i < strings.length; i++) {
                System.out.println(s + ":" + strings[i]);
            }
        }
        return "redirect:/manage";
    }
}

