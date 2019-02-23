package com.proxypool.util;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

/**
 * @program: muse-pay
 * @description: PhantomJSDriver
 * @author: Vincent
 * @create: 2019-02-16 15:40
 **/
public class PhantomJSDriverHelper {

    public static PhantomJSDriver getDriver() {
        //设置必要参数
        DesiredCapabilities dcaps = new DesiredCapabilities();
        //ssl证书支持
        dcaps.setCapability("acceptSslCerts", true);
        //截屏支持
        dcaps.setCapability("takesScreenshot", false);
        //css搜索支持
        dcaps.setCapability("cssSelectorsEnabled", true);

        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Content-Type", "text/html; charset=gbk");
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:65.0) Gecko/20100101 Firefox/65.0");
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Accept", "text/html,application/xhtml+xm…ml;q=0.9,image/webp,*/*;q=0.8");
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Accept-Encoding", "gzip, deflate, br");
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Connection", "keep-alive");

        //js支持
        dcaps.setJavascriptEnabled(true);

        //驱动支持
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                "D:\\chromedriver\\phantomjs-2.1.1-windows\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");

        PhantomJSDriver driver = new PhantomJSDriver(dcaps);
        return driver;
    }


    public static PhantomJSDriver setCookies(PhantomJSDriver driver, Map<String, String> cookieMap) {
        for (String key : cookieMap.keySet()) {
            // Cookie cookie = new Cookie(key, cookieMap.get(key), "/");

            // String name, String value, String domain, String path, Date expiry
            LocalDateTime now = LocalDateTime.now();
            now.plusMinutes(30L);

            LocalDateTime localDateTime = LocalDateTime.now();
            ZoneId zone = ZoneId.systemDefault();
            Instant instant = localDateTime.atZone(zone).toInstant();
            java.util.Date date = Date.from(instant);

            Cookie cookie = new Cookie(key, cookieMap.get(key), ".91160.com", "/", date);
            driver.manage().addCookie(cookie);
        }

        return driver;
    }


}
