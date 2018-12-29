package com.nucc.controller;

import com.alibaba.fastjson.JSON;
import com.nucc.entity.WXNotificationResponse;
import com.nucc.entity.weixin.WXBaseResponse;
import com.nucc.entity.weixin.WXTradeInfo;
import com.nucc.entity.weixin.requestentity.WXMerchantInfoVO;
import com.nucc.entity.weixin.requestentity.WXRefundInfoVO;
import com.nucc.service.weixin.WXMerchantInfoService;
import com.nucc.service.weixin.WXRefundInfoService;
import com.nucc.service.weixin.WXTradeInfoService;
import com.nucc.util.XMLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: muse-pay
 * @description: 网联微信控制类
 * @author: Vincent
 * @create: 2018-11-29 12:34
 **/
@RequestMapping("/nucc/weixin")
@RestController
public class NuccWeixinController {
    private Logger log = LoggerFactory.getLogger("NuccWeixinController");

    @Autowired
    private WXMerchantInfoService wxMerchantInfoService;

    @Autowired
    private WXTradeInfoService wxTradeInfoService;

    @Autowired
    private WXRefundInfoService wxRefundInfoService;


    @RequestMapping("/merchant/add")
    public String merchantAdd(HttpEntity<byte[]> requestEntity) {
        String result;
        try {
            // 解析参数
            WXMerchantInfoVO merchantInfoVO = parseParams(requestEntity);
            // 添加
            WXBaseResponse response = wxMerchantInfoService.add(merchantInfoVO);
            result = XMLUtils.objToXml(response);

        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }

        log.info("微信商户进件，返回内容=" + result);
        return result;
    }

    @RequestMapping("/merchant/update")
    public String merchantUpdate(HttpEntity<byte[]> requestEntity) {
        String result;
        try {
            // 解析参数
            WXMerchantInfoVO merchantInfoVO = parseParams(requestEntity);
            // 添加
            WXBaseResponse response = wxMerchantInfoService.update(merchantInfoVO);
            result = XMLUtils.objToXml(response);

        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }

        log.info("微信商户修改进件信息，返回内容=" + result);
        return result;
    }

    @RequestMapping("/merchant/query")
    public String merchantQuery(HttpEntity<byte[]> requestEntity) {
        String result;
        try {
            // 解析参数
            WXMerchantInfoVO merchantInfoVO = parseParams(requestEntity);
            // 添加
            WXBaseResponse response = wxMerchantInfoService.query(merchantInfoVO);
            result = XMLUtils.objToXml(response);

        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }

        log.info("查询微信商户，返回内容=" + result);
        return result;
    }

    /**
     * 解析参数
     *
     * @param requestEntity
     * @return
     * @throws Exception
     */
    private WXMerchantInfoVO parseParams(HttpEntity<byte[]> requestEntity) throws Exception {
        // 获取参数内容
        byte[] body = requestEntity.getBody();
        if (body == null) {
            throw new Exception("获取参数为空");
        }
        // 解析数据
        String xmlContent = new String(body, "UTF-8");
        log.info("收到参数=" + xmlContent);

        WXMerchantInfoVO merchantInfoVO = XMLUtils.xmlToObject(xmlContent, WXMerchantInfoVO.class);
        return merchantInfoVO;
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     */

    @RequestMapping("/pay/micropay")
    public String microPay(HttpEntity<byte[]> requestEntity) {
        String result;
        try {
            // 解析获取参数
            WXTradeInfo tradeInfo = parseWXTradeInfo(requestEntity);
            if (tradeInfo == null) {
                return XMLUtils.objToXml(WXBaseResponse.getErrResult("参数为空"));
            }
            // 支付
            WXBaseResponse response = wxTradeInfoService.micropay(tradeInfo);
            result = XMLUtils.objToXml(response);

        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }

        return result;
    }

    @RequestMapping("/pay/orderquery")
    public String orderQuery(HttpEntity<byte[]> requestEntity) {
        String result;
        try {
            // 解析获取参数
            WXTradeInfo tradeInfo = parseWXTradeInfo(requestEntity);
            if (tradeInfo == null) {
                return XMLUtils.objToXml(WXBaseResponse.getErrResult("参数为空"));
            }
            // 查询
            WXBaseResponse response = wxTradeInfoService.queryOrder(tradeInfo);
            result = XMLUtils.objToXml(response);

        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }

        return result;
    }

    @RequestMapping("/pay/ordercancel")
    public String orderCancel(HttpEntity<byte[]> requestEntity) {
        String result;

        try {
            // 解析获取参数
            WXTradeInfo tradeInfo = parseWXTradeInfo(requestEntity);
            if (tradeInfo == null) {
                return XMLUtils.objToXml(WXBaseResponse.getErrResult("参数为空"));
            }
            // 撤销订单
            WXBaseResponse response = wxTradeInfoService.cancelOrder(tradeInfo);
            result = XMLUtils.objToXml(response);

        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }

        return result;
    }

    @RequestMapping("/pay/unifiedorder")
    public String unifiedOrder(HttpEntity<byte[]> requestEntity) {
        String result;
        try {
            // 解析获取参数
            WXTradeInfo tradeInfo = parseWXTradeInfo(requestEntity);
            if (tradeInfo == null) {
                return XMLUtils.objToXml(WXBaseResponse.getErrResult("参数为空"));
            }
            // 撤销订单
            WXBaseResponse response = wxTradeInfoService.unifiedOrder(tradeInfo);
            result = XMLUtils.objToXml(response);

        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }

        return result;
    }

    @RequestMapping("/pay/closeorder")
    public String closeOrder(HttpEntity<byte[]> requestEntity) {
        String result;
        try {
            // 解析获取参数
            WXTradeInfo tradeInfo = parseWXTradeInfo(requestEntity);
            if (tradeInfo == null) {
                return XMLUtils.objToXml(WXBaseResponse.getErrResult("参数为空"));
            }
            // 撤销订单
            WXBaseResponse response = wxTradeInfoService.closeOrder(tradeInfo);
            result = XMLUtils.objToXml(response);

        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }

        return result;
    }

    @RequestMapping("/pay/refund")
    public String refund(HttpEntity<byte[]> requestEntity) {
        String result;
        try {
            // 解析获取参数
            WXRefundInfoVO refundInfoVO = parseWXRefundInfo(requestEntity);
            if (refundInfoVO == null) {
                return XMLUtils.objToXml(WXBaseResponse.getErrResult("参数为空"));
            }
            // 撤销订单
            WXBaseResponse response = wxRefundInfoService.add(refundInfoVO);
            result = XMLUtils.objToXml(response);

        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }

        return result;
    }

    @RequestMapping("/pay/singlerefundquery")
    public String refundQuery(HttpEntity<byte[]> requestEntity) {
        String result;
        try {
            // 解析获取参数
            WXRefundInfoVO refundInfoVO = parseWXRefundInfo(requestEntity);
            if (refundInfoVO == null) {
                return XMLUtils.objToXml(WXBaseResponse.getErrResult("参数为空"));
            }
            // 撤销订单
            WXBaseResponse response = wxRefundInfoService.querySingleRefund(refundInfoVO);
            result = XMLUtils.objToXml(response);

        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }

        return result;
    }

    @RequestMapping("/pay/allrefundquery")
    public String allRefundQuery(HttpEntity<byte[]> requestEntity) {
        String result;
        try {
            // 解析获取参数
            WXRefundInfoVO refundInfoVO = parseWXRefundInfo(requestEntity);
            if (refundInfoVO == null) {
                return XMLUtils.objToXml(WXBaseResponse.getErrResult("参数为空"));
            }
            // 撤销订单
            WXBaseResponse response = wxRefundInfoService.queryRefund(refundInfoVO);
            result = XMLUtils.objToXml(response);

        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }

        return result;
    }

    @RequestMapping("/pay/authcodetoopenid")
    public String authCodeToOpenId(HttpEntity<byte[]> requestEntity) {
        String result;
        try {
            // 解析获取参数
            WXTradeInfo tradeInfo = parseWXTradeInfo(requestEntity);
            if (tradeInfo == null) {
                return XMLUtils.objToXml(WXBaseResponse.getErrResult("参数为空"));
            }
            // 撤销订单
            WXBaseResponse response = wxTradeInfoService.authCodeToOpenId(tradeInfo);
            result = XMLUtils.objToXml(response);

        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }

        return result;
    }

    @RequestMapping("/pay/qrcode/{transactionId}")
    public WXBaseResponse qrcode(@PathVariable String transactionId) {
        try {
            return wxTradeInfoService.scanQrcode(transactionId);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("扫码支付，异常=" + e.getMessage());
        }

        return WXBaseResponse.getErrResult("扫码支付异常");
    }


    /**
     * 解析并生成微信交易参数
     *
     * @param requestEntity
     * @return
     * @throws Exception
     */
    private WXTradeInfo parseWXTradeInfo(HttpEntity<byte[]> requestEntity) throws Exception {
        byte[] body = requestEntity.getBody();
        if (body == null) {
            throw new Exception("获取参数为空");
        }
        // 解析数据
        String xmlContent = new String(body, "UTF-8");
        log.info("收到支付相关参数=" + xmlContent);

        return XMLUtils.xmlToObject(xmlContent, WXTradeInfo.class);
    }

    /**
     * 解析并生成微信交易参数
     *
     * @param requestEntity
     * @return
     * @throws Exception
     */
    private WXRefundInfoVO parseWXRefundInfo(HttpEntity<byte[]> requestEntity) throws Exception {
        byte[] body = requestEntity.getBody();
        if (body == null) {
            throw new Exception("获取参数为空");
        }
        // 解析数据
        String xmlContent = new String(body, "UTF-8");
        log.info("收到退款相关参数=" + xmlContent);

        return XMLUtils.xmlToObject(xmlContent, WXRefundInfoVO.class);
    }

    @RequestMapping("/notify")
    public String test(HttpEntity<byte[]> requestEntity) {
        try {
            byte[] body = requestEntity.getBody();
            if (body == null) {
                log.error("参数内容为空");
                return "FAIL";
            }

            // 解析数据
            String jsonContent = new String(body, "UTF-8");
            log.info("回调内容=" + jsonContent);
            WXNotificationResponse dataObj = JSON.parseObject(jsonContent, WXNotificationResponse.class);
            log.info("解析回调内容后的对象=" + dataObj);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "SUCCESS";
    }


}
