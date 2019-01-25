package com.muse.pay.dao;

import com.muse.pay.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Administrator
 * @Date: 2018 2018/7/22 10 37
 **/
@Service("userDao")
@Mapper
public interface UserInfoDao {

    UserInfo getUserById(@Param("id") int id);

    int updateUser(UserInfo user);

    int insertUser(UserInfo user);

    int deleteUserById(@Param("id") int id);

    UserInfo login(@Param("email") String email, @Param("password") String password);

    UserInfo selectUserByColumn(UserInfo userInfo);

    void updatePassword(@Param("userId") int userId, @Param("password") String password);

    List<UserInfo> getAllUser();

    List<Integer> getAllUserIds();

    List<Integer> getUserIdByName(String name);

    Integer getUserCountByEmail(@Param("email") String email);

    int setPwd(@Param("email") String email, @Param("password") String password);

    int modUserIcon(@Param("userId") int userId, @Param("iconUrl") String iconUrl);

    int modifyUserAmount(@Param("userId") int userId, @Param("amount") BigDecimal amount);


}
