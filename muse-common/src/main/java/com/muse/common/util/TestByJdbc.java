package com.muse.common.util;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: pay
 * @description: 数据库数据校验
 * @author: Vincent
 * @create: 2018-09-28 11:02
 **/
public class TestByJdbc {


    private static Connection getConn() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://rm-wz925uz3an76i7me0jo.mysql.rds.aliyuncs.com:3306/sywg?useUnicode=true&characterEncoding=UTF-8" +
                "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false&serverTimezone=GMT%2B8";
        String username = "sywg";
        String password = "Sywg@2018";
        Connection conn = null;
        try {
            Class.forName(driver);
            conn =  DriverManager.getConnection(url, username, password);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    public static Integer getAll() {
        Connection conn = getConn();
        List<BigDecimal[]> data = new ArrayList<>();
        //String sql = "SELECT create_time, amount, balance, user_no FROM rp_account_history WHERE  user_no='DDF0FC08F089B9F0D635EDC0EB714511E8E91FD49C01EE9189D8D7B909EF5672' ORDER BY create_time";
        String sql = "SELECT create_time, amount, balance FROM rp_account_history WHERE user_no=HEX(AES_ENCRYPT('QA88882018092810001218', 'sywg')) ORDER BY create_time";

        try {
            Statement pmt = conn.createStatement();
            ResultSet rs = pmt.executeQuery(sql);

            while (rs.next()) {
                BigDecimal amount = rs.getBigDecimal(2);
                BigDecimal balance = rs.getBigDecimal(3);

                data.add(new BigDecimal[]{amount, balance});
            }
            rs.close();
            pmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        for (int i=0; i<data.size()-1; i++) {
            BigDecimal[] curr = data.get(i);
            BigDecimal[] next = data.get(i+1);

            BigDecimal balance = curr[1].add(next[0]);
            if (!balance.equals(next[1])) {
                System.out.println("第" + i + "条记录到第" + (i+1) + "条记录异常");
                break;
            } else {
                System.out.println("第" + i + "个记录正确");
            }
        }

        return null;
    }

    public static void main(String[] args) {
        getAll();
    }
































}
