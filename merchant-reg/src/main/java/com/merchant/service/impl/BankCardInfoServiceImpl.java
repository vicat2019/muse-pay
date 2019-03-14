package com.merchant.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.merchant.dao.BankCardInfoMapper;
import com.merchant.entity.BankCardInfo;
import com.merchant.entity.FilePathTypeEnum;
import com.merchant.entity.ResultData;
import com.merchant.service.BankCardInfoService;
import com.merchant.service.BaseService;
import com.merchant.service.MerchantBaseService;
import com.merchant.util.AddressHelper;
import com.merchant.util.BankHelper;
import com.merchant.util.ExcelUtils;
import com.merchant.util.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;


/**
 * Created by Vincent on 2019/03/05.
 */
@Service("bankCardInfoService")
@Transactional
public class BankCardInfoServiceImpl extends BaseService<BankCardInfoMapper, BankCardInfo> implements BankCardInfoService {
    private Logger log = LoggerFactory.getLogger("BankCardInfoServiceImpl");

    @Autowired
    private BankHelper bankHelper;

    @Autowired
    private AddressHelper addressHelper;

    @Autowired
    private MerchantBaseService merchantBaseService;

    @Value("${UPLOAD_FOLDER_PATH}")
    private String UPLOAD_FOLDER_PATH;


    /**
     * 初始化二类卡信息
     *
     * @param excelPath     Excel文件路径
     * @param certPicFolder 证件照路径
     * @return ResultData
     * @throws Exception 异常
     */
    public ResultData initBankCard(String excelPath, String certPicFolder) throws Exception {

        // 可进件的处于激活状态的二类卡
        List<BankCardInfo> bankCardList = mapper.getAvailableBankCard();
        log.info("initBankCard() 库中已有可进件的二类卡数=" + bankCardList.size());

        // 从文件中读取
        List<BankCardInfo> fileBankCardList = getBankCardFromFile(excelPath);

        // 处理新增的二类卡信息
        List<BankCardInfo> newBankCardList = handleNewBankCard(fileBankCardList, certPicFolder);

        // 所有可进件的二类卡信息
        bankCardList.addAll(newBankCardList);

        // 返回结果
        log.info("initBankCard() 可用二类卡信息数=" + bankCardList.size());
        return ResultData.getSuccessResult(bankCardList);
    }


    /**
     * 处理新增的二类卡
     *
     * @param fileBankCardList 从文件中读取的二类卡信息
     * @param certPicFolder    新增的二类卡证件照目录
     * @return List
     */
    private List<BankCardInfo> handleNewBankCard(List<BankCardInfo> fileBankCardList, String certPicFolder) {
        // 查询数据库中所有二类卡
        List<BankCardInfo> allBankCardList = mapper.getAllBankCard();

        // 比较差集，保存到库中
        List<BankCardInfo> differenceSetList = differenceSet(allBankCardList, fileBankCardList);
        if (differenceSetList != null && differenceSetList.size() > 0) {
            // 上传二类卡的身份证件照
            differenceSetList = uploadBankCardCertPic(differenceSetList, certPicFolder);

            // 保存差集到数据库中
            mapper.insertByBatch(differenceSetList);
        }

        log.info("handleNewBankCard() 新增二类卡信息数=" + differenceSetList.size());
        return differenceSetList;
    }

    /**
     * 比较两个集合获取后一个集合不存在于源集合中的元素
     *
     * @param sourceList 源集合
     * @param objBList   比较集合
     * @return List
     */
    private List<BankCardInfo> differenceSet(List<BankCardInfo> sourceList, List<BankCardInfo> objBList) {
        if (sourceList == null && objBList != null) {
            return objBList;
        }
        if (objBList == null) {
            return null;
        }

        List<BankCardInfo> differenceSerList = Lists.newArrayList();

        for (BankCardInfo item : objBList) {
            if (!sourceList.contains(item)) {
                differenceSerList.add(item);
            }
        }
        log.info("differenceSet() 有" + differenceSerList.size() + "个数据不在源集合中");
        return differenceSerList;
    }

    /**
     * 从文件中读取二类卡信息
     *
     * @param excelPath 文件路径
     * @return List
     * @throws Exception 异常
     */
    private List<BankCardInfo> getBankCardFromFile(String excelPath) throws Exception {

        // 检查文件是否存在
        Preconditions.checkArgument(!StringUtils.isEmpty(excelPath), "文件路径不能为空");
        List<BankCardInfo> bankCardList = Lists.newArrayList();

        // 读取内容
        List<Object> fileDataList = ExcelUtils.readExcel(excelPath, 1, 2, "ALL");

        for (Object item : fileDataList) {
            List<String> infoList = (List<String>) item;

            // 某行数据为空
            if (infoList.get(0) == null) continue;

            // 基础信息
            String payName = infoList.get(1);
            String payPhone = infoList.get(2);
            String ecardNo = infoList.get(3);
            String cardNo = infoList.get(4);
            // 银行信息
            String bankName = infoList.get(5);
            String branchName = infoList.get(6);
            // 身份证
            String certNo = infoList.get(7);
            // 地址
            String province = infoList.get(8);
            String city = infoList.get(9);
            String area = infoList.get(10);
            String mchId = "00020023";

            // 获取银行和支行编码
            String bankCode = bankHelper.getBankCode(bankName);
            String branchCode = bankHelper.getBranchCode(bankCode, branchName);
            // 开户行省市区编码
            String provinceCode = addressHelper.getProvinceCode(province);
            String cityCode = addressHelper.getCityCode(provinceCode, city);
            String areaCode = addressHelper.getAreaCode(cityCode, area);

            // 生成二类卡对象
            BankCardInfo user = BankCardInfo.getInstance(mchId, payName, certNo, payPhone, cardNo, ecardNo,
                    bankCode, bankName, branchCode, branchName, provinceCode,
                    province, cityCode, city, areaCode, area,
                    "", "", "", "");
            // 补充信息
            user.setRegisterCount(0);
            user.setRegisterMaxCount(96);
            user.setStatus("ACTIVE");

            // 检查是否合格
            try {
                user.initCheckParams();
            } catch (Exception e) {
                log.error("getBankCardFromFile(), 异常信息=" + e.getMessage() + ", 用户信息=" + JSONObject.toJSONString(user));
                continue;
            }

            bankCardList.add(user);
        }

        log.info("getBankCardFromFile() 从文件中获取二类卡信息个数=" + bankCardList.size());
        return bankCardList;
    }

    /**
     * 处理用户的证件照图片地址
     *
     * @param bankCardList 用户列表
     * @return List
     */
    public List<BankCardInfo> uploadBankCardCertPic(List<BankCardInfo> bankCardList, String certPicFolderPath) {
        if (bankCardList == null) {
            log.error("uploadBankCardCertPic() 上传证件照片，二类卡列表为空");
            return null;
        }
        if (StringUtils.isEmpty(certPicFolderPath)) {
            log.error("uploadBankCardCertPic() 上传证件照片，证件照目录为空");
            return null;
        }

        List<BankCardInfo> newBankCardList = Lists.newArrayList();

        for (BankCardInfo bankCardInfo : bankCardList) {
            String userName = bankCardInfo.getPayName();

            // 证件照照片
            String certPicPath = certPicFolderPath + File.separator + userName;
            File certPicFile = new File(certPicPath);
            if (!certPicFile.exists()) {
                log.error("uploadBankCardCertPic() 用户[" + userName + "]的身份证照片目录不存在");
                continue;
            }

            // 正面照
            File frontFile = new File(certPicPath + File.separator + "1.jpg");
//            String frontUrl = FileHelper.uploadFile(frontFile);
            String frontUrl = "/test/1.jpg";
            bankCardInfo.setBuslicPic(frontUrl);
            bankCardInfo.setLegFrontPic(frontUrl);
            // 反面照
            File backFile = new File(certPicPath + File.separator + "2.jpg");
//            String backUrl = FileHelper.uploadFile(backFile);
            String backUrl = "/test/2.jpg";
            bankCardInfo.setLegBackPic(backUrl);
            // 手持照
            File handFile = new File(certPicPath + File.separator + "3.jpg");
//            String handUrl = FileHelper.uploadFile(handFile);
            String handUrl = "/test/3.jpg";
            bankCardInfo.setHandPic(handUrl);

            try {
                bankCardInfo.checkCertPic();
            } catch (Exception e) {
                log.error("uploadBankCardCertPic() 二类卡[" + userName + "]证件图片地址异常=" + e.getMessage());
                continue;
            }
            newBankCardList.add(bankCardInfo);
        }

        return newBankCardList;
    }


    @Override
    public ResultData add(BankCardInfo obj) throws Exception {
        if (obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        int result = mapper.insert(obj);
        return ResultData.getSuccessResult(result);
    }


    @Override
    public ResultData update(BankCardInfo obj) throws Exception {
        if (obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        if (obj.getId() == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        int result = mapper.updateByPrimaryKey(obj);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData get(Integer id) throws Exception {
        if (id == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        BankCardInfo result = mapper.selectByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData updateRegInfo(BankCardInfo bankCardInfo) {
        if (bankCardInfo == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        if (bankCardInfo.getId() == 0) {
            return ResultData.getErrResult("ID参数不能为空");
        }

        // 判断进件数是否已经等于最大进件数
        if (!bankCardInfo.isNeedToRegister()) {
            bankCardInfo.setStatus("FINISH");
        } else {
            bankCardInfo.setStatus("ACTIVE");
        }

        Integer result = mapper.updateRegInfo(bankCardInfo.getRegisterCount(), bankCardInfo.getStatus(),
                bankCardInfo.getId());
        log.info("更新二类卡进件信息=" + result);

        return ResultData.getSuccessResult(result);
    }

    @Override
    public ResultData del(Integer id) throws Exception {
        if (id == 0) {
            return ResultData.getErrResult("ID不能为空");
        }

        int result = mapper.deleteByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }

    /**
     * 上传文件
     *
     * @param file     上传的文件
     * @param request  请求内容
     * @param fileType 文件内容类型：1二类卡文件，2商户文件
     * @return ResultData
     * @throws Exception 异常
     */
    @Override
    public ResultData uploadDataFile(MultipartFile file, HttpServletRequest request, String fileType) throws Exception {
        // 检查参数，文件是否为空
        Preconditions.checkArgument(file != null, "上传的文件为空");

        // 接收上传的文件，获取子文件列表
        ResultData<List<File>> uploadResultData = FileHelper.uploadAndGetSubFileList(file, UPLOAD_FOLDER_PATH);
        if (!uploadResultData.whetherOk() || uploadResultData.resultIsEmpty()) {
            return uploadResultData;
        }
        List<File> uploadFileList = uploadResultData.getData();
        if (uploadFileList == null || uploadFileList.size() == 0) {
            throw new Exception("获取上传文件的子文件列表为空");
        }

        // 是否还有一级目录
        List<File> subFileList;
        if (uploadFileList.size() == 1 && uploadFileList.get(0).isDirectory()) {
            subFileList = FileHelper.getSubFileList(uploadFileList.get(0));
        } else {
            subFileList = Lists.newArrayList(uploadFileList);
        }

        // 检查文件夹内容格式
        File excelFile = checkBankCardData(subFileList);

        // 根据不同的文件内容类型进行不同的处理
        if ("1".equals(fileType)) {
            // 读取文件
            List<BankCardInfo> bankCardList = getBankCardFromFile(excelFile.getAbsolutePath());
            // 检查二类卡和证件照文件夹是否匹配
            checkBankCardCertFile(bankCardList, excelFile.getParent());
            // 处理新增的二类卡信息(上传图片，保存到库中)
            handleNewBankCard(bankCardList, excelFile.getParent());

        } else if ("2".equals(fileType)) {
            String doorPicFolder = "";
            for (File item : subFileList) {
                if (item.isDirectory()) {
                    doorPicFolder = item.getAbsolutePath();
                }
            }
            if (StringUtils.isEmpty(doorPicFolder)) {
                throw new Exception("获取门头照目录路径异常");
            }
            merchantBaseService.initMerchantBase(excelFile.getAbsolutePath(),
                    FilePathTypeEnum.EXCEL_FILE, doorPicFolder);
        }

        // 删除文件


        // 返回结果
        return ResultData.getSuccessResult();
    }

    @Override
    public List<BankCardInfo> getAvailableBankCard() throws Exception {
        List<BankCardInfo> bankCardList = mapper.getAvailableBankCard();
        if (bankCardList == null) {
            throw new Exception("查询可进件的二类卡信息为空");
        }
        return bankCardList;
    }

    /**
     * 检查二类卡文件夹格式
     *
     * @param fileList 文件夹内容列表
     * @return File
     * @throws Exception 异常
     */
    private File checkBankCardData(List<File> fileList) throws Exception {
        // 检查文件是否为空
        Preconditions.checkArgument(fileList != null && fileList.size() > 0, "二类卡文件为空");

        // 文件格式数量是否为空
        int excelFileCount = 0;
        int folderCount = 0;
        File excelFile = null;
        for (File file : fileList) {
            if (file.isFile() && FileHelper.isExcelFile(file.getName())) {
                excelFileCount += 1;
                excelFile = file;
            } else if (file.isDirectory()) {
                folderCount += 1;
            }
        }
        if (excelFileCount != 1 || folderCount == 0) {
            throw new Exception("文件夹内容不正确，应该包含一个Excel文件和若干个文件夹");
        }

        return excelFile;
    }

    /**
     * 检查二类卡和证件照是否匹配
     *
     * @param bankCardList 二类卡信息列表
     * @param folder       证件照目录
     * @throws Exception 异常
     */
    private void checkBankCardCertFile(List<BankCardInfo> bankCardList, String folder) throws Exception {
        // 检查参数
        Preconditions.checkArgument(bankCardList != null && bankCardList.size() > 0, "二类卡信息列表为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(folder), "文件夹路径参数为空");

        File folderFile = new File(folder);
        if (!folderFile.exists()) {
            throw new Exception("二类卡证件照目录不存在");
        }
        File[] certFileAr = folderFile.listFiles();
        if (certFileAr == null) {
            throw new Exception("二类卡证件照目录为空");
        }

        List<String> certFolderNameList = Lists.newArrayList();
        for (File item : certFileAr) {
            certFolderNameList.add(item.getName());
        }
        List<String> notCertList = Lists.newArrayList();
        for (BankCardInfo bankCard : bankCardList) {
            if (!certFolderNameList.contains(bankCard.getPayName())) {
                notCertList.add(bankCard.getPayName());
            }
        }

        if (notCertList.size() > 0) {
            throw new Exception("以下二类卡没有上传证件照图片:" + Joiner.on(", ").join(notCertList));
        }
    }


}
