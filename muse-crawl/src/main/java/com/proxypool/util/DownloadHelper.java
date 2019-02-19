package com.proxypool.util;

import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: muse-pay
 * @description: 下载工具
 * @author: Vincent
 * @create: 2019-02-18 17:09
 **/
public class DownloadHelper {
    private static String url = "https://www.skuimg.com/u/20190215/10260129.jpg";

    public static void main(String[] args) {
        String a = "";
        getFolderName(a);
    }

    public static String getFolderName(String content) {
        String name;

        Pattern pattern = Pattern.compile("\\](.+)\\[.+P\\]");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String groupStr = matcher.group(1);
            name = groupStr.replaceAll("\\s+", "");
            System.out.println(name);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            name = sdf.format(new Date());
            System.out.println("匹配失败, 使用生成的文件名称=" + name);
        }
        return name;
    }


    public static boolean start(String url, String destFile) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault(); // 创建httpClient实例
            HttpGet httpGet = new HttpGet(url); // 创建httpget实例
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:65.0) Gecko/20100101 Firefox/65.0");
            httpGet.setHeader("Accept", "text/html,application/xhtml+xm…ml;q=0.9,image/webp,*/*;q=0.8");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate, br");
            httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
            httpGet.setHeader("Connection", "keep-alive");
            httpGet.setHeader("Cookie", "__cfduid=d9f490f8b83d4f4091b6eb5b13624817a1550483763");

            CloseableHttpResponse response = httpClient.execute(httpGet); // 执行http get请求

            InputStream in = response.getEntity().getContent();
            FileOutputStream out = new FileOutputStream(new File(destFile));
            byte[] data = new byte[512];

            int c;
            while ((c = in.read(data)) > 0) {
                out.write(data, 0, c);
            }
            in.close();
            out.close();

            response.close(); // response关闭
            httpClient.close(); // httpClient关闭

        } catch (ParseException | IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


}
