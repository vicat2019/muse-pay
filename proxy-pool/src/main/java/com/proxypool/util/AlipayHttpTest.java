package com.proxypool.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @program: muse-pay
 * @description: 测试发送查询账务明细的请求
 * @author: Vincent
 * @create: 2018-12-08 10:13
 **/
public class AlipayHttpTest {


    public static void main(String[] args) {

        String url = "https://mbillexprod.alipay.com/enterprise/fundAccountDetail.json";

        Map<String, String> cookies = new HashMap<>();
        cookies.put("3FV_lastvisit", "2300 1544179602 /read.php?tid=6766&fid=72");
        cookies.put("ali_apache_tracktmp", "\"uid=2088331361132272\"");
        cookies.put("alipay", "\"K1iSL1p05ue3iKYq1xxo4t0nnhnw66kxKU9jqGyTw0RXjYY=\"");
        cookies.put("ALIPAYJSESSIONID", "RZ182Yx3p6awYpjrsnaj9f64WoPJohauthRZ17GZ00");
        cookies.put("CLUB_ALIPAY_COM", "2088331361132272");
        cookies.put("cna", "8bF+FDWUTTQCAT2NScwUifSa");
        cookies.put("CNZZDATA1253107971", "1366611954-1544173788-https%3A%2F%2Fauthsu121.alipay.com%2F|1544238588");
        cookies.put("credibleMobileSendTime", "-1");
        cookies.put("ctoken", "6c0wYP13wlHdt0Tw");
        cookies.put("ctuMobileSendTime", "-1");
        cookies.put("isg", "BHd3Glhu4LBoSGQ86bo_9yXPBWsBlEv9qJYEUcklFcateJW60Qhl73qRXhiDiyMW");
        cookies.put("iw.userid", "\"K1iSL1p05ue3iKYq1xxo4g==\"");
        cookies.put("JSESSIONID", "66CE0C20FDB659525CB6149CC3B8E155");
        cookies.put("LoginForm", "alipay_login_auth");
        cookies.put("mobileSendTime", "-1");
        cookies.put("riskCredibleMobileSendTime", "-1");
        cookies.put("riskMobileAccoutSendTime", "-1");
        cookies.put("riskMobileBankSendTime", "-1");
        cookies.put("riskMobileCreditSendTime", "-1");
        cookies.put("riskOriginalAccountMobileSendTime", "-1");
        cookies.put("rtk", "xe8SGhOre9pzkDpMleWYE02S1MCIaDPrrOyg1UOSufMwaDdHlXM");
        cookies.put("session.cookieNameId", "ALIPAYJSESSIONID");
        cookies.put("spanner", "gxuCMKqC9FnRjImXwHjBbw9xCw/FcenYXt2T4qEYgj0=");
        cookies.put("UM_distinctid", "1673fd863559-0f67ee0ab81bfe8-4c312979-1fa400-1673fd863561b0");
        cookies.put("zone", "GZ00C");

        Map<String, String> params = new HashMap<>();
        params.put("_input_charset", "gbk");
        params.put("billUserId", "2088331361132272");
        params.put("ctoken", "6c0wYP13wlHdt0Tw");
        params.put("endDateInput", "2018-12-08+00:00:00");
        params.put("order", "descend");
        params.put("pageNum", "1");
        params.put("pageSize", "20");
        params.put("precisionQueryKey", "tradeNo");
        params.put("queryEntrance", "1");
        params.put("showType", "0");
        params.put("sortTarget", "tradeTime");
        params.put("sortType", "0");
        params.put("startDateInput", "2018-12-07+00:00:00");
        params.put("total", "0");
        params.put("type", "");

        System.out.println(post(url, params, cookies));
    }

    public static String post(String url, Map<String, String> map, Map<String, String> cookies) {
        try {
            HttpPost httpPost = new HttpPost(url);

            // 设置参数
            List<NameValuePair> list = new ArrayList<>();
            if (map != null && map.size() > 0) {
                Iterator iterator = map.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                    list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
                    System.out.println("请求的参数为:" + elem.getKey() + ":" + elem.getValue());
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(list, "GBK"));

            //设置头部信息
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=GBK");
            httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
            httpPost.setHeader("Referer", "http://huaban.com/kctsmohcqk/following/");

            BasicCookieStore cookieStore = new BasicCookieStore();
            if (cookies != null && cookies.size() > 0) {
                for (String key : cookies.keySet()) {
                    String value = cookies.get(key);

                    BasicClientCookie cookie = new BasicClientCookie(key, value);
                    cookie.setVersion(0);
                    cookie.setDomain("alipay.com");   //设置范围
                    cookie.setPath("/");

                    cookieStore.addCookie(cookie);
                }
            }

            //CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

            RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
            httpPost.setConfig(defaultConfig);

            HttpResponse response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();

            return entityToString(entity);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private static String entityToString(HttpEntity entity) throws IOException {
        String result = null;
        if (entity != null) {
            long lenth = entity.getContentLength();
            if (lenth != -1 && lenth < 2048) {
                result = EntityUtils.toString(entity, "GBK");
            } else {
                InputStreamReader reader1 = new InputStreamReader(entity.getContent(), "GBK");
                CharArrayBuffer buffer = new CharArrayBuffer(2048);
                char[] tmp = new char[1024];
                int l;
                while ((l = reader1.read(tmp)) != -1) {
                    buffer.append(tmp, 0, l);
                }
                result = buffer.toString();
            }
        }
        return result;
    }


}
