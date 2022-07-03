package com.atguigu.orderservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.orderservice.entity.Order;
import com.atguigu.orderservice.entity.PayLog;
import com.atguigu.orderservice.mapper.PayLogMapper;
import com.atguigu.orderservice.service.OrderService;
import com.atguigu.orderservice.service.PayLogService;
import com.atguigu.orderservice.utils.HttpClient;
import com.atguigu.orderservice.utils.PayTypeConstant;
import com.atguigu.orderservice.utils.WeChatPayConstant;
import com.atguigu.servicebase.handler.exception.CustomException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author Quan
 * @since 2021-08-08
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Resource
    private OrderService orderService;

    @Override
    public Map<String, Object> createNative(String orderNo) {
        try {
            // 1. 通过orderNo查询订单详情
            Order order = orderService.getOrderInfo(orderNo);

            //2.使用map设置生成二维码需要的参数
            Map<String, String> reqMap = new HashMap<>();
            //设置支付参数
            reqMap.put("appid", WeChatPayConstant.APPID);
            reqMap.put("mch_id", WeChatPayConstant.PARTNER);
            reqMap.put("nonce_str", WXPayUtil.generateNonceStr());
            reqMap.put("body", order.getCourseTitle());
            reqMap.put("out_trade_no", orderNo);
            reqMap.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue() + "");
            reqMap.put("spbill_create_ip", "127.0.0.1");
            reqMap.put("notify_url", WeChatPayConstant.NOTIFYURL);
            reqMap.put("trade_type", "NATIVE");

            //3.发送httpclient请求，传递参数
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            client.setXmlParam(WXPayUtil.generateSignedXml(reqMap, WeChatPayConstant.PARTNERKEY));
            client.setHttps(true);
            //执行请求发送
            client.post();

            //4.得到发送请求返回结果
            //返回的内容是xml格式
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //封装返回结果集
            Map<String, Object> map = new HashMap<>();
            map.put("out_trade_no", orderNo);
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            //返回二维码状态码
            map.put("result_code", resultMap.get("result_code"));
            //二维码地址
            map.put("code_url", resultMap.get("code_url"));
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(20001, "支付失败");
        }
    }

    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            //1、封装参数
            Map<String, String> reqMap = new HashMap<>();
            reqMap.put("appid", WeChatPayConstant.APPID);
            reqMap.put("mch_id", WeChatPayConstant.PARTNER);
            reqMap.put("out_trade_no", orderNo);
            reqMap.put("nonce_str", WXPayUtil.generateNonceStr());

            // 2. 请求接口
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(reqMap, WeChatPayConstant.PARTNERKEY));
            client.setHttps(true);
            client.post();

            // 3. 获取订单结果
            String content = client.getContent();
            return WXPayUtil.xmlToMap(content);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(20001, "查询失败");
        }
    }

    @Override
    public void updateOrderStatus(Map<String, String> map) {
        // 1. 修改订单状态
        String outTradeNo = map.get("out_trade_no");
        Order order = orderService.getOrderInfo(outTradeNo);
        // 如果已经支付过了就不用继续了
        if (order.getStatus().equals(PayTypeConstant.PAID)) {
            return;
        }
        order.setStatus(PayTypeConstant.PAID);
        orderService.updateById(order);

        // 2. 添加记录
        PayLog payLog = new PayLog();
        payLog.setOrderNo(map.get("out_trade_no"));
        payLog.setPayTime(new Date());
        payLog.setTotalFee(order.getTotalFee());
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setTradeState(map.get("trade_state"));
        payLog.setPayType(order.getPayType());
        // 将微信回调结果转成字符串存入数据库
        payLog.setAttr(JSONObject.toJSONString(map));
        this.save(payLog);
    }
}
