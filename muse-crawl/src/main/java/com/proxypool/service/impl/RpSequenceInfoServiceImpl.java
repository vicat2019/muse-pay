package com.proxypool.service.impl;

import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.proxypool.dao.RpSequenceInfoMapper;
import com.proxypool.entry.RpSequenceInfo;
import com.proxypool.service.RpSequenceInfoService;
import com.proxypool.util.RedisUtil;
import com.proxypool.util.SequenceUtil;
import com.proxypool.util.TextUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by Vincent on 2018/12/17.
 */
@Service("sequenceInfoService")
@Transactional
public class RpSequenceInfoServiceImpl extends BaseService<RpSequenceInfoMapper, RpSequenceInfo> implements RpSequenceInfoService {
    private Logger log = LoggerFactory.getLogger("RpSequenceInfoServiceImpl");

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public String test() throws Exception {
        List<Object> test = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            test.add(TextUtils.getRandomStr(10) + "-" + (i + 1));
        }
        redisUtil.lSetAll("list3", test);

        return (String) redisTemplate.opsForList().leftPop("list3");
    }

    private ReentrantLock lock = new ReentrantLock(true);

    private static int count = 0;

    /**
     * 获取序列
     *
     * @return String
     * @throws Exception 异常
     */
    @Override
    public String obtainSequence() throws Exception {

        // 随机获取缓存序列的key
        String cacheKey = RpSequenceInfo.obtainRedisKey();
        Object obj = redisUtil.lLeftPop(cacheKey);


        if (obj == null) {
            lock.lock();
            try {
                obj = redisUtil.lLeftPop(cacheKey);
                if (obj == null) {
                    count += 1;
                    boolean cacheResult = genAndCacheSequences(cacheKey, 50000);
                    log.info("obtainSequence() 生成数据并缓存到Redis中，结果=" + cacheResult);
                    // 再取一次数据
                    obj = redisUtil.lLeftPop(cacheKey);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }


        log.info("需要批量生成序列号次数[count] = " + count);
        if (obj instanceof String) {
            String sequence = (String) obj;
            updateSequenceStatus(sequence);
            return sequence;
        } else {
            log.error("obtainSequence() 获取序列号失败");
            return "";
        }
    }

    /**
     * 更新已经获取的序列的状态
     *
     * @param sequence 序列号
     * @return int
     */
    private int updateSequenceStatus(String sequence) {
        int count = mapper.updateBySequence("1", sequence);
        log.info("obtainSequence() 从数据库中删除序列=" + sequence + "， 结果=" + count);

        return count;
    }

    /**
     * 生成并缓存序列号
     *
     * @param cacheKey 缓存KEY
     * @param genCount 生成个数
     * @return boolean
     */
    private boolean genAndCacheSequences(String cacheKey, int genCount) {

        // 查询序列数据
        List<Object> sequenceList = mapper.selectSequenceBatch(genCount);
        log.info("genAndCacheSequences() 从数据库中查询序列数据，个数=" + (sequenceList != null ? sequenceList.size() : 0));

        // 生成序列
        if (sequenceList == null || sequenceList.size() == 0) {
            String currentIndexStr = mapper.getCurrentSequenceNum("current_sequence_num");
            if (StringUtils.isEmpty(currentIndexStr)) {
                currentIndexStr = "1679616";
            }
            int currentIndex = Integer.valueOf(currentIndexStr);

            // 生成记录，并添加到数据库中
            long start = System.currentTimeMillis();
            sequenceList = genAndSaveSequence(currentIndex, genCount);
            log.info("genAndCacheSequences() 生成" + genCount + "个序列号并保存，耗时=" + ((System.currentTimeMillis() - start)
                    / 1000d));
        }

        // 缓存到Redis中
        if (sequenceList != null && sequenceList.size() > 0) {
            redisUtil.del(cacheKey);
            boolean cacheResult = redisUtil.lSetAll(cacheKey, sequenceList);
            log.info("genAndCacheSequences() 设置序列集合到缓存中，key=" + cacheKey + ", 个数=" + sequenceList.size()
                    + ", 结果=" + cacheResult + Thread.currentThread().getName());
            sequenceList.clear();
        }

        return true;
    }

    /**
     * 生成序列集合并保存到数据库中
     *
     * @param currentNum 当前个数
     * @param count      个数
     * @return List
     */
    private List<Object> genAndSaveSequence(int currentNum, int count) {
        // 存储集合
        List<Object> sequenceList = new ArrayList<>();
        List<String> tempList = new ArrayList<>();

        // 遍历，生成序列
        int sequenceNum = currentNum;
        for (int i = 1; i <= count; i++) {
            // 序列对应的数字
            sequenceNum = currentNum + i;
            // 根据数字生成序列
            tempList.add(SequenceUtil.numToSequence(sequenceNum));

            // 每满1000个则保存一次
            if (i % 1000 == 0) {
                sequenceList.addAll(tempList);
                int result = mapper.insertSequenceBatch(tempList);
                log.info("genAndSaveSequence() 添加序列到数据库，结果=" + result + ", 个数=" + tempList.size());
                tempList.clear();
            }
        }
        // 插入数据库表中
        if (tempList.size() > 0) {
            int result = mapper.insertSequenceBatch(tempList);
            sequenceList.addAll(tempList);
            log.info("genAndSaveSequence() 添加序列到数据库，结果=" + result + ", 个数=" + tempList.size());
        }

        // 更新
        int updateResult = mapper.updateCurrentSequenceNum(String.valueOf(sequenceNum), "current_sequence_num");
        log.info("genAndSaveSequence() 更新当前数据标识=" + sequenceNum + ", 结果=" + updateResult);

        log.info("genAndSaveSequence() 添加序列到数据库, 生成序列号个数=" + count + ", 当前currentIndex=" + sequenceNum);
        // 返回结果
        return sequenceList;
    }


    @Override
    public ResultData add(RpSequenceInfo obj) throws Exception {
        if (obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        int result = mapper.insert(obj.getSequenceNo());
        return ResultData.getSuccessResult(result);
    }


    @Override
    public ResultData update(String sequenceNo, String status) throws Exception {
        int result = mapper.updateBySequence(sequenceNo, status);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData get(String sequence) throws Exception {
        if (StringUtils.isEmpty(sequence)) {
            return ResultData.getErrResult("sequence不能为空");
        }

        RpSequenceInfo result = mapper.selectBySequence(sequence);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData del(String sequence) throws Exception {
        if (StringUtils.isEmpty(sequence)) {
            return ResultData.getErrResult("sequence不能为空");
        }

        int result = mapper.deleteBySequence(sequence);
        return ResultData.getSuccessResult(result);
    }
}
