package com.proxypool.service;

import com.muse.common.entity.ResultData;
import com.proxypool.entry.RpSequenceInfo;

/**
 * Created by Vincent on 2018/12/17.
 */
public interface RpSequenceInfoService {

    ResultData add(RpSequenceInfo obj) throws Exception;

    ResultData del(String sequence) throws Exception;

    ResultData update(String sequenceNo, String status) throws Exception;

    ResultData get(String sequence) throws Exception;

    String obtainSequence() throws Exception;

    String test() throws Exception;
}
