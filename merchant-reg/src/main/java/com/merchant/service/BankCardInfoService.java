package com.merchant.service;


import com.merchant.entity.BankCardInfo;
import com.merchant.entity.ResultData;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Vincent on 2019/03/05.
 */
public interface BankCardInfoService {

    ResultData initBankCard(String excelPath, String certPicFolder) throws Exception;

    ResultData add(BankCardInfo obj) throws Exception;

    ResultData del(Integer id) throws Exception;

    ResultData update(BankCardInfo obj) throws Exception;

    ResultData get(Integer id) throws Exception;

    ResultData updateRegInfo(BankCardInfo bankCardInfo);

    ResultData uploadDataFile(MultipartFile file, HttpServletRequest request, String fileType) throws Exception;

    List<BankCardInfo> getAvailableBankCard() throws Exception;


}
