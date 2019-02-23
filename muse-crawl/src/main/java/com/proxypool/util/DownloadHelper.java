package com.proxypool.util;

import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;

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
        String a = "https://www.91160.com/dep/show/depid-3826.html";
        start(a);
    }

    public static String getFolderName(String content) {
        String name;

        Pattern pattern = Pattern.compile("(.+)\\[.+P\\]");
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
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault(); // 创建httpClient实例
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
                if (httpClient != null) httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    public static boolean start(String url) {
        CloseableHttpClient httpClient = null;
        try {
            CookieStore cookieStore = getCookieStore();

            // 创建httpClient实例
            httpClient = HttpClients.createDefault();

            // 创建httpget实例
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:65.0) Gecko/20100101 Firefox/65.0");
            httpGet.setHeader("Accept", "text/html,application/xhtml+xm…ml;q=0.9,image/webp,*/*;q=0.8");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate, br");
            httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
            httpGet.setHeader("Connection", "keep-alive");
            httpGet.setHeader("Cookie", cookieStore.toString());

            CloseableHttpResponse response = httpClient.execute(httpGet);
            InputStream in = response.getEntity().getContent();
            FileOutputStream out = new FileOutputStream(new File("d:\\doctor.txt"));
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
                if (httpClient != null) httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private static CookieStore getCookieStore() {
        CookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie("__guid", "MijNWz5c709f39737562.26040785");
        cookie.setVersion(0);
        cookie.setPath("/");
        BasicClientCookie cookie2 = new BasicClientCookie("__jsluid", "bc46d490240b92ccbd29fea887c0dbfe");
        cookie2.setVersion(0);
        cookie2.setPath("/");
        BasicClientCookie cookie3 = new BasicClientCookie("gr_user_id", "4288344b-21e1-47d9-9dfc-8c9bc13b1664");
        cookie3.setVersion(0);
        cookie3.setPath("/");
        BasicClientCookie cookie4 = new BasicClientCookie("FISKCDDCC", "7fe30d8c46661d9d724b540783246e10");
        cookie4.setVersion(0);
        cookie4.setPath("/");
        BasicClientCookie cookie5 = new BasicClientCookie("gr_session_id_88c697c1877e5045", "08f51ba5-0a09-492e-871d-de0568363e71");
        cookie5.setVersion(0);
        cookie5.setPath("/");
        BasicClientCookie cookie6 = new BasicClientCookie("gr_session_id_88c697c1877e5045_08f51ba5-0a09-492e-871d-de0568363e71", "true");
        cookie6.setVersion(0);
        cookie6.setPath("/");
        BasicClientCookie cookie7 = new BasicClientCookie("Hm_lpvt_c4e8e5b919a5c12647962ea08462e63b", "1550885276");
        cookie7.setVersion(0);
        cookie7.setPath("/");
        BasicClientCookie cookie8 = new BasicClientCookie("Hm_lvt_c4e8e5b919a5c12647962ea08462e63b", "1550884666");
        cookie8.setVersion(0);
        cookie8.setPath("/");
        BasicClientCookie cookie9 = new BasicClientCookie("ip_city", "sz");
        cookie9.setVersion(0);
        cookie9.setPath("/");
        BasicClientCookie cookie10 = new BasicClientCookie("is_read_index_notice_", "1");
        cookie10.setVersion(0);
        cookie10.setPath("/");

        cookieStore.addCookie(cookie);
        cookieStore.addCookie(cookie2);
        cookieStore.addCookie(cookie3);
        cookieStore.addCookie(cookie4);
        cookieStore.addCookie(cookie5);
        cookieStore.addCookie(cookie6);
        cookieStore.addCookie(cookie7);
        cookieStore.addCookie(cookie8);
        cookieStore.addCookie(cookie9);
        cookieStore.addCookie(cookie10);
        return cookieStore;
    }


}
