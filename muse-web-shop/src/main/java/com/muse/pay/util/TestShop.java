package com.muse.pay.util;

import com.google.common.collect.Lists;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Administrator
 * @Date: 2018 2018/7/25 11 49
 **/
public class TestShop {

    public static void main(String[] args) {
        List<String> bookIdList = Lists.newArrayList();
        bookIdList.add("");
        bookIdList.add("a");
        bookIdList.add("");
        bookIdList.add("g");
        bookIdList.add("c");

        bookIdList = bookIdList.stream().filter(s -> !StringUtils.isEmpty(s)).collect(Collectors.toList());

        System.out.println(bookIdList.size());


        bookIdList.forEach(s -> System.out.println("-" + s + "-"));
    }
}
