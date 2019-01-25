package com.muse.common.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * @Author: Administrator
 * @Date: 2018 2018/8/8 17 43
 **/
public class QrcodeUtils {

    public static void main(String[] args) {
        makeQrcode("http://www.baidu.com");
    }

    public static String makeQrcode(String url) {

        String folder = "D:\\java_workspace\\qrcode\\";
        String fileName = System.currentTimeMillis() + ".png";
        String filePath = folder + fileName;

        int width = 300;
        int height = 300;
        String format = "png";
        //定义二维码的参数
        HashMap map = new HashMap();
        //设置编码
        map.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //设置纠错等级
        map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        map.put(EncodeHintType.MARGIN, 2);

        try {
            //生成二维码
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height);
            Path file = new File(filePath).toPath();
            MatrixToImageWriter.writeToPath(bitMatrix, format, file);

        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileName;
    }



































}
