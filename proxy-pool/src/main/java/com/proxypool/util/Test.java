package com.proxypool.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: muse-pay
 * @description: 测试类
 * @author: Vincent
 * @create: 2018-10-15 19:32
 **/
public class Test {

    public static void main(String[] args) {



        /*String ip = "36.48.73.16";
        int port = 80;
        try {
            useProxy(ip, port, "https://www.baidu.com", "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        /*Instant instant = new Date().toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime nowLocalDateTime = instant.atZone(zoneId).toLocalDateTime();

        LocalDateTime time = LocalDateTime.of(2018, 10, 28, 9, 28, 42);

        Duration duration = java.time.Duration.between(time, nowLocalDateTime);
        long interval = duration.toDays();
        System.out.println(interval);*/



        // genRandomInfo(50000);

        List<Integer> a = new ArrayList<>();
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
        a.add(5);
        a.add(6);
        a.add(7);
        a.add(8);
        a.add(9);
        a.add(10);

        /*a = a.stream().filter(item -> item <5).collect(Collectors.toList());
        a.forEach(System.out::println);*/

        a.subList(2,6).forEach(System.out::println);
    }


    private static void genRandomInfo(int size) {
        File file = new File("d:/logs/secKill.txt");

        try {
            FileOutputStream out = new FileOutputStream(file);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

            int userId = 1;
            while (size > 0) {
                writer.write(1000 + "," + userId + "\n");
                size--;
                userId ++;
            }
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void useProxy(String ip, int port, String targetUrl, String encode) throws Exception {
        //创建httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建httpGet实例
        HttpGet httpGet = new HttpGet(targetUrl);
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

        if (response != null) {
            HttpEntity entity = response.getEntity();  //获取返回实体
            if (entity != null) {
                System.out.println("网页内容为:" + EntityUtils.toString(entity, encode));
            }
        }
        if (response != null) {
            response.close();
        }
        if (httpClient != null) {
            httpClient.close();
        }
    }
















































}
