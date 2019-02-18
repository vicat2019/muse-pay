package com.proxypool.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @program: muse-pay
 * @description: Guava缓存
 * @author: Vincent
 * @create: 2019-01-09 10:30
 **/
public class GuavaCacheUtil {
    private static Logger log = LoggerFactory.getLogger("GuavaCacheUtil");

    /**
     * 缓存
     **/
    public static LoadingCache<String, List<Integer>> cache = CacheBuilder.newBuilder()
            .maximumSize(5000)
            .expireAfterAccess(5 * 60L, TimeUnit.SECONDS)
            .build(createCacheLoader());

    /**
     * 当缓存为空时加载数据
     *
     * @return CacheLoader
     */
    private static CacheLoader<String, List<Integer>> createCacheLoader() {
        return new CacheLoader<String, List<Integer>>() {
            @Override
            public List<Integer> load(String key) throws Exception {
                /*if ("code".equals(key)) {
                    MeBookService meBookService = (MeBookService) SpringBeanUtils.getBean("meBookService");
                    List<Integer> codeList = meBookService.getAllCode();
                    log.info("guava加载数据, key=" + key + ", static=" + (codeList != null ? codeList.size() : 0));
                    return codeList;
                } else {
                    log.info("guava加载数据, key=" + key);
                    return null;
                }*/
                return null;
            }
        };
    }
}
