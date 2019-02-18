package com.proxypool.util;

import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

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
        //js支持
        dcaps.setJavascriptEnabled(true);
        //驱动支持
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                "D:\\chromedriver\\phantomjs-2.1.1-windows\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");

        PhantomJSDriver driver = new PhantomJSDriver(dcaps);
        return driver;
    }

}
