package com.muse.common.util;

import com.alibaba.fastjson.JSON;
import com.muse.common.entity.ResultData;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Administrator
 * @Date: 2018 2018/8/13 18 23
 **/
@Component("httpUtils")
public class HttpUtils {
    private static Logger log = LoggerFactory.getLogger("HttpUtils");

    @Autowired
    private RestTemplate restTemplate;

    public static void main(String[] args) {

        String url2 = "https://www.ip.cn/";

        try {
            ResultData<Map<String, Object>> result = HttpUtils.getByProxy("221.7.255.168", 8080, url2);
            if (result.whetherOk() && result.resultIsNotEmpty()) {
                System.out.println(result.getData());
            } else {
                System.out.println(result.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送HTTP请求
     *
     * @param url       请求地址
     * @param resultMap 请求参数
     * @return String
     * @throws Exception 异常
     */
    public ResultData post(String url, Map<String, Object> resultMap) throws Exception {
        String bodyValTemplate = getValueForStr(resultMap);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity entity = new HttpEntity(bodyValTemplate, headers);
        ResponseEntity response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        log.info("发送请求返回状态=" + response.getStatusCode());
        if (response.getStatusCode() != HttpStatus.OK) {
            return ResultData.getErrResult("请求失败[" + response.getStatusCode().toString() + "]");
        }

        String content = (response.getBody() != null ? response.getBody().toString() : "");
        log.info("返回内容=" + (content != null ? content : "返回内容为空"));

        return ResultData.getSuccessResult((Object) content);
    }

    /**
     * 发送HTTP请求
     *
     * @param url 请求地址
     * @return String
     * @throws Exception 异常
     */
    public ResultData post(String url, String dataStr) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity entity = new HttpEntity(JSON.toJSON(dataStr), headers);
        ResponseEntity response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        log.info("发送请求返回状态=" + response.getStatusCode());
        if (response.getStatusCode() != HttpStatus.OK) {
            return ResultData.getErrResult("请求失败[" + response.getStatusCode().toString() + "]");
        }

        String content = (response.getBody() != null ? response.getBody().toString() : "");
        log.info("返回内容=" + (content != null ? content : "返回内容为空"));

        return ResultData.getSuccessResult((Object) content);
    }

    /**
     * 发送GET请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return ResultData
     */
    public ResultData get(String url, Map<String, Object> params) {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, params);

        if (response.getStatusCode() != HttpStatus.OK) {
            return ResultData.getErrResult("请求失败[" + response.getStatusCode().toString() + "]");
        }
        String content = (response.getBody() != null ? response.getBody() : "");
        log.info("返回内容=" + (content != null ? content : "返回内容为空"));

        return ResultData.getSuccessResult((Object) content);
    }

    /**
     * 发送GET请求
     *
     * @return ResultData
     */
    public ResultData getByProxy(String url, String host, int port, String type) {
        try {
            RestTemplate restTemplateProxy = restTemplateProxy(host, port, type);

            long start = System.currentTimeMillis();
            ResponseEntity<String> response = restTemplateProxy.getForEntity(url, String.class);
            long end = System.currentTimeMillis();

            if (response.getStatusCode() != HttpStatus.OK) {
                return ResultData.getErrResult("请求失败[" + response.getStatusCode().toString() + "]");
            }
            return ResultData.getSuccessResult((end - start) / 1000d);

        } catch (Exception e) {
            log.error("发送HTTP请求异常=" + e.getMessage());
            return ResultData.getErrResult(e.getMessage());
        }
    }

    /**
     * 使用代理
     *
     * @param ip
     * @param port
     * @throws Exception
     */
    public static ResultData<Map<String, Object>> getByProxy(String ip, int port, String url) throws Exception {
        //创建httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建httpGet实例
        HttpGet httpGet = new HttpGet(url);
        //设置代理IP，设置连接超时时间 、 设置 请求读取数据的超时时间 、 设置从connect Manager获取Connection超时时间、
        HttpHost proxy = new HttpHost(ip, port);
        RequestConfig requestConfig = RequestConfig.custom()
                .setProxy(proxy)
                .setConnectTimeout(10000)
                .setSocketTimeout(10000)
                .setConnectionRequestTimeout(3000)
                .build();
        httpGet.setConfig(requestConfig);
        //设置请求头消息
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                "Chrome/62.0.3202.94 Safari/537.36");
        CloseableHttpResponse response = httpClient.execute(httpGet);

        Map<String, Object> resultMap = new HashMap<>();
        ResultData<Map<String, Object>> resultData;

        if (response != null) {
            // 请求的状态
            int statusCode = response.getStatusLine().getStatusCode();
            // 获取返回实体
            org.apache.http.HttpEntity entity = response.getEntity();

            // 获取返回的内容
            String responseContent = "";
            if (entity != null) {
                responseContent = EntityUtils.toString(entity, "utf-8");
                // writeToFile(responseContent);
            }
            resultMap.put("status", statusCode);
            resultMap.put("content", responseContent);

            // 生成返回对象
            if (statusCode == HttpStatus.OK.value()) {
                resultData = ResultData.getSuccessResult();
            } else {
                resultData = ResultData.getErrResult();
            }
            resultData.setData(resultMap);

            log.info("请求地址：" + url + "，返回内容=" + responseContent);
        } else {
            resultData = ResultData.getErrResult("Response为空");
        }

        if (response != null) {
            response.close();
        }
        httpClient.close();

        return resultData;
    }

    private static void writeToFile(String content) {
        File defaultFile = new File("d:/logs/temp.html");
        if (defaultFile.exists()) {
            defaultFile.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(defaultFile);
            out.write(content.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 以String的方式返回值
     *
     * @return String
     */
    private String getValueForStr(Map<String, Object> valueMap) throws Exception {
        String resultStr = "";
        for (Map.Entry item : valueMap.entrySet()) {
            resultStr += item.getKey() + "=" + URLEncoder.encode(item.getValue().toString(), "utf-8") + "&";
        }
        if (resultStr.length() > 1) {
            resultStr = resultStr.substring(0, resultStr.length() - 1);
        }
        return resultStr;
    }


    public RestTemplate restTemplateProxy(String host, int port, String type) {
        RestTemplate template = new RestTemplate(simpleClientHttpRequestFactoryProxy(host, port, type));
        template.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }


    public ClientHttpRequestFactory simpleClientHttpRequestFactoryProxy(String host, int port, String type) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(30000);       //单位为ms
        factory.setConnectTimeout(10000);    //单位为ms

        SocketAddress address = new InetSocketAddress(host, port);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
        factory.setProxy(proxy);

        return factory;
    }

}
