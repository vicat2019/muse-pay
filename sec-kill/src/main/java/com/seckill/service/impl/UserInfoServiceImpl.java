package com.seckill.service.impl;

import com.muse.common.entity.ResultData;
import com.muse.common.service.BaseService;
import com.muse.common.util.GenRandomPersonInfo;
import com.seckill.dao.UserInfoMapper;
import com.seckill.entity.UserInfo;
import com.seckill.service.UserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * Created by Vincent on 2018/12/24.
 */
@Service("userInfoService")
@Transactional
public class UserInfoServiceImpl extends BaseService<UserInfoMapper, UserInfo> implements UserInfoService {

    @Override
    public ResultData add(UserInfo obj) throws Exception {
        if(obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        int result = mapper.insert(obj);
        return ResultData.getSuccessResult(result);
    }


    @Override
    public ResultData update(UserInfo obj) throws Exception {
        if(obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        if(obj.getId() == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        int result = mapper.updateByPrimaryKey(obj);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData get(Integer id) throws Exception {
        if(id == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        UserInfo result = mapper.selectByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData genRandomUser(int size) throws Exception {
        Random random = new Random();
        List<UserInfo> userList = new ArrayList<>();

        for (int i=0; i<size; i++) {
            Map<String, Object> personMap = GenRandomPersonInfo.getPersonInfo();
            UserInfo user = new UserInfo();
            user.setName((String) personMap.get("name"));
            user.setAddress((String) personMap.get("road"));
            user.setAge(random.nextInt(100));
            user.setSex((String) personMap.get("sex"));

            userList.add(user);
        }

        mapper.insertBatch(userList);

        return null;
    }

    @Override
    public ResultData del(Integer id) throws Exception {
        if(id == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        int result = mapper.deleteByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }
}
