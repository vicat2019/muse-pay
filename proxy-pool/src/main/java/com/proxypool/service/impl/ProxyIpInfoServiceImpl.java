package com.proxypool.service.impl;


import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.muse.common.util.HttpUtils;
import com.proxypool.dao.ProxyIpInfoMapper;
import com.proxypool.entry.ProxyIpInfo;
import com.proxypool.service.ProxyIpInfoService;
import com.proxypool.util.DateUtil;
import com.proxypool.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @Description: 代理信息服务类
 * @Author: Vincent
 * @Date: 2018/10/15
 */
@Service("proxyIpInfoService")
@Transactional
public class ProxyIpInfoServiceImpl extends BaseService<ProxyIpInfoMapper, ProxyIpInfo> implements ProxyIpInfoService {
    private Logger log = LoggerFactory.getLogger("ProxyIpInfoServiceImpl");

    // 线程池
    private static ThreadPoolExecutor executorPool = new ThreadPoolExecutor(10, 50, 30,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(3000));

    // 用于检查代理地址可用性使用的地址
    private static Map<String, String> webUrlMap = new HashMap<>();
    private static List<String> webUrlList = new ArrayList<>();

    static {
        webUrlMap.put("http://www.baidu.com", "baidu");
        webUrlMap.put("http://www.qq.com", "qq");
        webUrlMap.put("http://www.163.com", "163");
        webUrlMap.put("http://www.sina.com", "sina");
        webUrlMap.put("http://www.hao123.com", "hao123");
        webUrlMap.put("http://www.soho.com", "soho");
        webUrlMap.put("http://www.douyu.com", "douyu");
        webUrlMap.put("http://www.ganji.com", "ganji");
        webUrlMap.put("http://www.douban.com/", "douban");
        webUrlMap.put("http://www.mtime.com", "mtime");
        webUrlMap.put("http://www.huanqiu.com/", "huanqiu");
        webUrlMap.put("http://www.jjwxc.net/", "jjwxc");
        webUrlMap.put("http://www.tuniu.com/", "tuniu");
        webUrlMap.put("http://www.dianping.com/", "dianping");

        webUrlMap.keySet().forEach(item -> webUrlList.add(item));
    }

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private Executor asyncServiceExecutor;


    @Override
    public ResultData add(ProxyIpInfo obj) throws Exception {
        if (obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        if (!DateUtil.isLegalIp(obj.getIp())) {
            return ResultData.getErrResult("IP值异常=" + obj.getIp());
        }
        int thisIpCount = getCountByIp(obj.getIp());
        if (thisIpCount > 0) {
            return ResultData.getErrResult("该IP地址已经存在[" + obj.getIp() + "]");
        }

        int result = mapper.insert(obj);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public int getCountByIp(String ip) throws Exception {
        if (StringUtils.isEmpty(ip)) {
            return 0;
        }
        if (!DateUtil.isLegalIp(ip)) {
            return 0;
        }
        return mapper.getCountByIp(ip);
    }

    @Override
    public List<ProxyIpInfo> getUsableProxyIp(int size) throws Exception {
        // 如果没有限定获取多少个可用IP，则获取前200个
        if (size <= 0 || size >= 100000) {
            size = 200;
        }
        List<ProxyIpInfo> usableIpList = mapper.getUsableProxyIp(size);
        if (usableIpList != null && usableIpList.size() > 0) {
            return usableIpList;
        }
        return null;
    }

    @Override
    public void updateProxyIpStatusInfo(ProxyIpInfo proxyIpInfo) throws Exception {
        if (proxyIpInfo == null) {
            return;
        }
        int count = mapper.updateStatusInfo(proxyIpInfo);
        log.info("updateProxyIpStatusInfo() 更新代理IP信息结果信息=" + count);
    }

    @Override
    public int setProxyIpStatus(int id, String status) throws Exception {
        if (id == 0 || StringUtils.isEmpty(status)) {
            return 0;
        }
        return mapper.setProxyIpStatus(id, status);
    }

    /**
     * 检查代理地址是否可用
     *
     * @param size 检查的数量
     * @return ResultData
     * @throws Exception 异常
     */
    @Override
    public ResultData checkAvailability(int size) throws Exception {
        // 如果参数异常，则默认检查1000
        if (size <= 0 || size >= 100000) {
            size = 1000;
        }

        // 获取要检查的代理地址列表(根据时间倒序)
        List<ProxyIpInfo> proxyIpList = mapper.getRecentlyIp(size);
        log.info("要检查的代理地址个数= " + (proxyIpList == null ? 0 : proxyIpList.size()) + " 个");
        if (proxyIpList == null || proxyIpList.size() == 0) {
            return ResultData.getErrResult("代理地址集合为空");
        }

        for (ProxyIpInfo proxyIpInfo : proxyIpList) {
            executorPool.execute(() -> {
                try {
                    // 随机获取请求地址
                    Random random = new Random();
                    String url = webUrlList.get(random.nextInt(webUrlList.size()));
                    // 发送请求
                    boolean checkResult = httpGetByProxy(proxyIpInfo, url, webUrlMap.get(url));
                    // 更新代理记录状态
                    setProxyIpStatus(proxyIpInfo.getId(), (checkResult ? "SUCCESS" : "FAIL"));

                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("检查代理IP异常[" + e.getMessage() + "]");
                }
            });
        }

        return ResultData.getSuccessResult();
    }

    @Override
    public int setStatusByIp(String ip, String status) throws Exception {
        if (!StringUtils.isEmpty(ip) && !StringUtils.isEmpty(status)) {
            return mapper.setStatusByIp(ip, status);
        }

        return 0;
    }

    /**
     * 发送请求
     *
     * @param proxyIpInfo  代理地址信息
     * @param url          请求的URL
     * @param regexContent 匹配的关键字
     * @return boolean
     * @throws Exception 异常
     */
    private boolean httpGetByProxy(ProxyIpInfo proxyIpInfo, String url, String regexContent) {
        try {
            // 设置代理
            RestTemplate restTemplateProxy = httpUtils.restTemplateProxy(proxyIpInfo.getIp(),
                    Integer.parseInt(proxyIpInfo.getPort()),
                    proxyIpInfo.getType());
            // 发送请求
            ResponseEntity<String> response = restTemplateProxy.getForEntity(url, String.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                log.error("请求失败, 请求IP=" + proxyIpInfo.getIp() + ", 地址=" + url + ", 状态=[" + response.getStatusCode().toString() + "]");
            }
            // 返回内容
            String content = response.getBody();
            if (!StringUtils.isEmpty(content)) {
                content = new String(content.getBytes("ISO8859-1"), "utf-8");
            }
            // 匹配关键字
            if (StringUtils.isEmpty(TextUtils.getMatch("(" + regexContent + ")", content))) {
                log.error("使用代理发送请求[失败]，代理信息=" + proxyIpInfo.getIp() + ", " + proxyIpInfo.getPort());
                return false;
            } else {
                log.info("使用代理发送请求[成功]，代理信息=" + proxyIpInfo.getIp() + ", " + proxyIpInfo.getPort());
                return true;
            }
        } catch (Exception e) {
            log.error("使用代理IP发送请求异常=" + e.getMessage());
            return false;
        }
    }


    @Override
    public ResultData update(ProxyIpInfo proxy) throws Exception {
        if (proxy == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        if (proxy.getId() == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        // 获取对象
        ProxyIpInfo target = mapper.selectByPrimaryKey(proxy.getId());
        if (target != null) {
            target.setStatus(proxy.getStatus());
            return ResultData.getSuccessResult(mapper.updateByPrimaryKey(target));
        } else {
            return ResultData.getErrResult("更新失败");
        }
    }

    @Override
    public ResultData get(Integer id) throws Exception {
        if (id == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        ProxyIpInfo result = mapper.selectByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData getAllProxyCode() throws Exception {
        List<String> proxyCodeList = mapper.getAllProxyCode();
        if (proxyCodeList != null) {
            return ResultData.getSuccessResult(proxyCodeList);
        }

        return ResultData.getSuccessResult(new ArrayList<>());
    }

    @Override
    public ResultData del(Integer id) throws Exception {
        if (id == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        int result = mapper.deleteByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }


    @Override
    public void checkProxyIp() {
        List<Integer> delIdList = new ArrayList<>();

        // 获取IP地址
        List<ProxyIpInfo> proxyList = mapper.queryProxy();
        if (proxyList != null && proxyList.size() > 0) {
            proxyList.forEach(item -> asyncServiceExecutor.execute(() -> {
                String ip = item.getIp();
                String port = item.getPort();
                String type = item.getType();
                log.debug("检查IP信息=" + ip + ":" + port);

                // 检查参数是否为空
                if (!StringUtils.isEmpty(ip) && !StringUtils.isEmpty(port)) {
                    int portInt = Integer.parseInt(port.trim());

                    ResultData result = httpUtils.getByProxy("http://www.baidu.com", ip, portInt, type);
                    log.debug("请求返回结果=" + result.getCode() + ", " + result.getMessage());

                    if (!result.isOk()) {
                        item.setStatus("FAIL");
                    } else {
                        item.setStatus("SUCCESS");
                        double spendTime = (double) result.getData();
                        try {
                            item.setResponseSpeed(String.valueOf(spendTime));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        item.setSuccessCount(item.getSuccessCount() + 1);
                        item.setLastSuccessTime(DateUtil.getToday());
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    item.setLastCheckTime(sdf.format(new Date()));
                    item.setCheckCount(item.getCheckCount() + 1);
                    mapper.updateByPrimaryKey(item);

                    // 处理失败超过条件的记录
                    handleMaxErrIp(item, delIdList);
                }
            }));
        }

        // 处理失败超过条件的记录
        handleErrIpById(delIdList);
    }

    @Override
    public ResultData getFrontProxy() throws Exception {
        List<ProxyIpInfo> proxyList = mapper.getFrontProxy();
        if (proxyList == null || proxyList.size() == 0) {
            return ResultData.getErrResult("没有合适的代理信息");
        }

        return ResultData.getSuccessResult(proxyList);
    }

    /**
     * 处理错误代理信息
     *
     * @param proxyIpInfo 代理
     * @param delIdList   软删除代理信息IP集合
     */
    private void handleMaxErrIp(ProxyIpInfo proxyIpInfo, List<Integer> delIdList) {
        if (proxyIpInfo == null) return;
        boolean isDel = false;

        // 如果错误次数占总检查次数的 30%，且上次成功的时间距离当前时间超过2天，则标记该记录为删除状态
        // 标记为删除状态的记录，不再进行检查，只是作为添加时检查是否已经存在
        if (proxyIpInfo.getCheckCount() > 0) {
            // 失败率
            double rate = (proxyIpInfo.getCheckCount() - proxyIpInfo.getSuccessCount()) / (double) proxyIpInfo.getCheckCount();
            if (rate >= 0.5) {
                // 检查上次成功的时间间隔
                if (proxyIpInfo.getLastSuccessTime() != null) {
                    Date now = new Date();
                    Instant instant = now.toInstant();
                    ZoneId zoneId = ZoneId.systemDefault();
                    LocalDateTime nowLocalDateTime = instant.atZone(zoneId).toLocalDateTime();

                    Duration duration = java.time.Duration.between(proxyIpInfo.getLastSuccessTime(), nowLocalDateTime);
                    long interval = duration.toDays();
                    isDel = interval >= 1;
                } else {
                    if (proxyIpInfo.getCheckCount() >= 5) isDel = true;
                }
            }

            // 删除数据
            if (isDel) delIdList.add(proxyIpInfo.getId());
        }
    }

    /**
     * 软删除代理信息
     *
     * @param delIdList 软删除代理信息IP集合
     */
    private void handleErrIpById(List<Integer> delIdList) {
        if (delIdList != null && delIdList.size() > 0) {
            int count = mapper.delProxyIpSoft(delIdList);
            log.info("软删除代理信息，个数=" + count);
        }
    }

    @Override
    public ResultData delRepeatProxy() {
        List<ProxyIpInfo> repeatProxyList = mapper.getRepeatProxy();

        List<Integer> delIdList = new ArrayList<>();
        ProxyIpInfo lastProxy = repeatProxyList.get(0);
        for (int i = 1; i < repeatProxyList.size(); i++) {
            ProxyIpInfo tempProxy = repeatProxyList.get(i);
            if (lastProxy.getIp().equals(tempProxy.getIp())) {
                delIdList.add(tempProxy.getId());
            }

            lastProxy = tempProxy;
        }
        System.out.println("重复的记录数=" + repeatProxyList.size() + "， 要删除的重复记录数=" + delIdList.size());

        if (delIdList.size() > 0) {
            if (delIdList.size() > 1000) {
                int count;
                if (delIdList.size() % 1000 == 0) {
                    count = delIdList.size() / 1000;
                } else {
                    count = delIdList.size() / 1000 + 1;
                }

                for (int i = 0; i < count; i++) {
                    int start = 1000 * i;
                    int end = 1000 * (i + 1);
                    if (end > delIdList.size()) end = delIdList.size() - 1;

                    int delCount = mapper.delProxyByIds(delIdList.subList(start, end));
                    System.out.println("删除记录数=" + delCount);

                }

            } else {
                mapper.delProxyByIds(delIdList);
            }
        }
        return ResultData.getSuccessResult("删除重复记录" + delIdList.size() + "个");
    }


}
