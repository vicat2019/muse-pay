package com.merchant.controller;

import com.merchant.entity.FilePathTypeEnum;
import com.merchant.entity.ResultData;
import com.merchant.service.impl.BatchRegisterServiceImpl;
import com.merchant.util.MyTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: merchant-register
 * @description:
 * @author: Vincent
 * @create: 2019-02-14 09:32
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private BatchRegisterServiceImpl batchRegister;


    /**
     * 批量进件
     *
     * @param location
     * @return
     */
    @RequestMapping("/batch/{location}")
    public ResultData register(@PathVariable("location") String location) {
        // 门头照目录
        String doorPicFolder;
        String mchExcelPath;
        String bankCardExcel;
        String bankCardCertPic;
        if (location.equals("sy")) {
            doorPicFolder = "C:\\Users\\Administrator\\Desktop\\滨农进件资料\\沈阳\\沈阳二类卡进件-20190302\\商户-沈阳-0302\\进件商户门头照-沈阳-20190302";
            mchExcelPath = "C:\\Users\\Administrator\\Desktop\\滨农进件资料\\沈阳\\沈阳二类卡进件-20190302\\商户-沈阳-0302\\进件商户信息-沈阳-20190302.xlsx";
            bankCardExcel = "";
            bankCardCertPic = "";
        } else {
            doorPicFolder = "E:\\滨农进件资料\\本地\\20190214天津滨海进件资料2\\20190214天津滨海进件资料2";
            mchExcelPath = doorPicFolder;

            bankCardExcel = "E:\\滨农进件资料\\本地\\二类卡进件信息\\本地二类户银行资料.xlsx";
            bankCardCertPic = "E:\\滨农进件资料\\本地\\二类卡进件信息";
        }

        try {
            return batchRegister.execute(bankCardExcel, bankCardCertPic, mchExcelPath,
                    FilePathTypeEnum.DOOR_PIC_FOLDER, doorPicFolder);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultData.getErrResult();
    }


    @Autowired
    private MyTest myTest;

    @RequestMapping("/t")
    public ResultData test() {
        try {
            myTest.main();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultData.getSuccessResult();
    }


}
