package com.muse.pay.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analysis {



    public static void main(String[] args) {

/*        String filePath = "C:\\Users\\Administrator\\Desktop\\并发测试\\log\\0903\\pay.2018-09-03.19.log";
        analysisTime(filePath);

        String gatewayLog = "C:\\Users\\Administrator\\Desktop\\并发测试\\log\\0903\\roncoo-pay-web-gateway.log";
        analysisInteval(gatewayLog);*/

        analysisInteval("d:\\data_15.log");
    }



    private static void analysisTime(String filePath) {
        double total = 0d;
        double sts = 0d;
        double stg = 0d;
        double stm = 0d;
        int count = 0;
        int stsCount = 0;
        int stgCount = 0;
        int stmCount = 0;

        // 读取文件
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));

            Pattern concurrencyPattern = Pattern.compile("CONCURRENCY耗时=(\\d+\\.?\\d*)");
            Pattern stsPattern = Pattern.compile("STS耗时=(\\d+\\.?\\d*)");
            Pattern stgPattern = Pattern.compile("STG耗时=(\\d+\\.?\\d*)");
            Pattern stmPattern = Pattern.compile("STM耗时=(\\d+\\.?\\d*)");

            String line;
            while ((line=in.readLine()) != null) {
                Matcher concurrencyMatcher = concurrencyPattern.matcher(line);
                Matcher stsMatcher = stsPattern.matcher(line);
                Matcher stgMatcher = stgPattern.matcher(line);
                Matcher stmMatcher = stmPattern.matcher(line);

                if (stsMatcher.find()) {
                    double value = Double.parseDouble(stsMatcher.group(1));
                    stsCount += 1;
                    sts += value;
                }
                if (stgMatcher.find()) {
                    double value = Double.parseDouble(stgMatcher.group(1));
                    stgCount += 1;
                    stg += value;
                }
                if (stmMatcher.find()) {
                    double value = Double.parseDouble(stmMatcher.group(1));
                    stmCount += 1;
                    stm += value;
                }
                if (concurrencyMatcher.find()) {
                    double value = Double.parseDouble(concurrencyMatcher.group(1));
                    count += 1;
                    total += value;
                }
            }
            in.close();

            System.out.println("STS共有记录=" + stsCount + "个，总耗时=" + sts + "，平均耗时=" + (sts / stsCount));
            System.out.println("STG共有记录=" + stgCount + "个，总耗时=" + stg + "，平均耗时=" + (stg / stgCount));
            System.out.println("STM共有记录=" + stmCount + "个，总耗时=" + stm + "，平均耗时=" + (stm / stmCount));
            System.out.println("CCY共有记录=" + count + "个，总耗时=" + total + "，平均耗时=" + (total / count));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void analysisInteval(String filePath) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));

            Pattern pattern = Pattern.compile("支付回调通知处理_PART_1_03_03=(\\d+\\.?\\d*)");

            double time = 0L;

            int count = 0;
            String line;
            while ((line=in.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    double timestamp = Double.parseDouble(matcher.group(1));
                    time += timestamp;
                    count += 1;
                }
            }
            in.close();

            System.out.println("总耗时=" + time + ", 请求次数=" + count + ", 平均耗时=" + (time / count));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



































}
