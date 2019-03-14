package com.merchant.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.merchant.dao.MerchantBaseInfoMapper;
import com.merchant.entity.FilePathTypeEnum;
import com.merchant.entity.MerchantBaseInfo;
import com.merchant.entity.ResultData;
import com.merchant.service.BaseService;
import com.merchant.service.MerchantBaseService;
import com.merchant.util.AddressHelper;
import com.merchant.util.CommonUtils;
import com.merchant.util.ExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * Created by Vincent on 2019/03/06.
 */
@Service("merchantBaseService")
@Transactional
public class MerchantBaseServiceImpl extends BaseService<MerchantBaseInfoMapper, MerchantBaseInfo>
        implements MerchantBaseService {
    private Logger log = LoggerFactory.getLogger("MerchantBaseServiceImpl");

    @Autowired
    private AddressHelper addressHelper;

    @Autowired
    private MerchantBaseService merchantBaseInfoService;


    @Override
    public ResultData add(MerchantBaseInfo obj) throws Exception {
        if (obj == null) {
            return ResultData.getErrResult("参数不能为空");
        }
        int result = mapper.insert(obj);
        return ResultData.getSuccessResult(result);
    }


    @Override
    public ResultData update(MerchantBaseInfo obj) throws Exception {
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

        MerchantBaseInfo result = mapper.selectByPrimaryKey(id);
        return ResultData.getSuccessResult(result);
    }

    @Override
    public List<MerchantBaseInfo> getUnusedMerchantBase() {
        return mapper.getUnusedMerchantBase();
    }

    @Override
    public int insertByBatch(List<MerchantBaseInfo> merchantBaseList) {
        if (merchantBaseList == null || merchantBaseList.size() == 0) {
            return 0;
        }

        int total = 0;
        if (merchantBaseList.size() > 1000) {
            List<List<MerchantBaseInfo>> parts = Lists.partition(merchantBaseList, 1000);
            for (List<MerchantBaseInfo> list : parts) {
                total += mapper.insertByBatch(list);
            }
        } else {
            total = mapper.insertByBatch(merchantBaseList);
        }

        log.info("批量添加商户基本信息数据=" + total);
        return total;
    }

    @Override
    public int updateStatus(String status, int id) {
        return mapper.updateStatus(status, id);
    }

    @Override
    public int softDeleteById(int id) {
        return mapper.softDeleteById(id);
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
     * 格式化商户名称
     *
     * @param name         名称
     * @param originalName 原始名称
     * @return String
     */
    @Override
    public String formatMerchantName(String name, String originalName) {
        // 过滤关键词
        if (name.contains("车臣")) {
            name = name.replaceAll("车臣", "CC");
        }
        if (name.contains("武侯祠")) {
            name = name.replaceAll("武侯祠", "WHC");
        }
        if (name.contains("大师")) {
            name = name.replaceAll("大师", "DS");
        }
        if (name.contains("按摩")) {
            name = name.replaceAll("按摩", "AM");
        }
        if (name.contains("无界")) {
            name = name.replaceAll("无界", "WJ");
        }
        if (name.contains("&")) {
            name = name.replaceAll("\\&", "");
        }

        // 截取字母
        if (name.length() >= 10) {
            name = handleLetter(name);
        }
        // 去掉"直营店"
        if (name.length() >= 10) {
            name = name.replaceAll("直营店", "");
        }
        // 去掉"店"
        if (name.length() >= 10) {
            name = name.replaceAll("店", "");
        }
        // 去掉"中心"
        if (name.length() >= 10) {
            name = name.replaceAll("中心", "");
        }
        // 去掉括号，截取字母
        if (name.length() >= 10) {
            String temp = name.replaceAll("（", "");
            temp = temp.replaceAll("（", "");
            temp = temp.replaceAll("）", "");
            temp = temp.replaceAll("\\(", "");
            temp = temp.replaceAll("\\)", "");
            if (temp.length() < 10) {
                name = temp;
            }
        }

        // 去掉括号和里面内容，
        if (name.length() >= 10) {
            name = originalName.replaceAll("（.+）", "");
            name = name.replaceAll("\\(.+\\)", "");
        }
        // 截取名称中的前9个字符
        if (name.length() >= 10) {
            name = name.substring(0, 9);
        }

        // 特殊处理
        if (name.equals("7天连锁酒店")) name = "7天连锁酒店二";
        if (name.equals("SELECTED")) name = "SELECTED2";
        if (name.equals("starfish蓝")) name = "starfis2蓝";
        if (name.equals("Superdry")) name = "Superdry2";
        if (name.toUpperCase().equals("UCC国际洗衣")) {
            if (originalName.length() > 7) {
                name = originalName.replaceAll("洗衣", "");
                if (name.length() >= 10) {
                    name = name.replaceAll("\\(", "");
                    name = name.replaceAll("\\)", "");
                    name = name.substring(0, 9);
                }
            }
        }
        // 去掉空格
        name = name.replaceAll("\\s+", "");

        // 去重名
        // Offwhite
        if (name.equals("Offwhite")) name = "Offwhit" + CommonUtils.genRandomStr(2);
        // UCC国际洗衣
        if (name.equals("UCC国际洗衣")) name = "UCC国际洗衣" + CommonUtils.genRandomStr(2);
        // 伊蕾名店
        if (name.equals("伊蕾名店")) name = "伊蕾名店" + CommonUtils.genRandomStr(5);
        // 依尚街区
        if (name.equals("依尚街区")) name = "依尚街区" + CommonUtils.genRandomStr(5);
        // 利郎
        if (name.equals("利郎")) name = "利郎" + CommonUtils.genRandomStr(7);
        // 劲霸男装
        if (name.equals("劲霸男装")) name = "劲霸男装" + CommonUtils.genRandomStr(5);
        // 埃沃裁缝
        if (name.equals("埃沃裁缝")) name = "埃沃裁缝" + CommonUtils.genRandomStr(5);
        // 城市便捷酒店
        if (name.equals("城市便捷酒店")) name = "城市便捷酒店" + CommonUtils.genRandomStr(3);

        if (name.equals("城市空间·蘭会所")) name = "城市空间蘭会所" + CommonUtils.genRandomStr(2);
        if (name.equals("城市空间云会所足道")) name = "城市空间云会所" + CommonUtils.genRandomStr(2);
        if (name.equals("城门口老火锅中航")) name = "城门口老火锅中航" + CommonUtils.genRandomStr(1);
        if (name.equals("城门口老火锅")) name = "城门口老火锅" + CommonUtils.genRandomStr(3);
        if (name.equals("城门口老火锅太升")) name = "城门口老火锅太升" + CommonUtils.genRandomStr(1);
        if (name.equals("培贝体智能开发中心")) name = "培贝体智能开发" + CommonUtils.genRandomStr(2);
        if (name.equals("壹捷汽车服务")) name = "壹捷汽车服务" + CommonUtils.genRandomStr(3);
        if (name.equals("天空之城")) name = "天空之城" + CommonUtils.genRandomStr(5);
        if (name.equals("好声音量贩KTV")) name = "好声音量贩" + CommonUtils.genRandomStr(4);
        if (name.equals("如家酒店")) name = "如家酒店" + CommonUtils.genRandomStr(5);

        // 去掉符号，只保留中文、数字、字母
        name = name.replaceAll("[^\u4e00-\u9fa5a-zA-Z0-9]", "");

        return name;
    }

    /**
     * 压缩商铺名称中的字母
     *
     * @param name 名称
     * @return String
     */
    private static String handleLetter(String name) {
        Pattern pattern = Pattern.compile("([a-zA-Z]+)");
        Matcher matcher = pattern.matcher(name);
        String result = name;
        if (matcher.find()) {
            result = name.replaceAll("\\s+", "");
            String temp = matcher.group(1);
            if (temp.length() > 2) {
                temp = temp.substring(0, 2);
            }
            result = result.replaceAll("[a-zA-Z]+|\\.+|\\&+", temp.toUpperCase());
        }
        return result;
    }

    /**
     * 从门头照图片文件夹中获取商铺名称
     *
     * @param filePath Excel路径
     * @param pathType 路径类型
     * @return List
     */
    @Override
    public List<MerchantBaseInfo> getMerchantBase(String filePath, FilePathTypeEnum pathType, String doorPicFolder) throws Exception {

        List<MerchantBaseInfo> merchantBaseList = Lists.newArrayList();

        // 从文件中获取新数据
        if (!StringUtils.isEmpty(filePath)) {
            ResultData handleFileDataResult = initMerchantBase(filePath, pathType, doorPicFolder);
            if (!handleFileDataResult.whetherOk() || handleFileDataResult.resultIsEmpty()) {
                throw new Exception("获取文件中数据为空");
            }
            List<MerchantBaseInfo> newMchBaseList = (List<MerchantBaseInfo>) handleFileDataResult.getData();
            merchantBaseList.addAll(newMchBaseList);
        }

        // 查询数据库中原有数据
        List<MerchantBaseInfo> dbMchBaseList = mapper.getUnusedMerchantBase();
        if (dbMchBaseList != null) {
            merchantBaseList.addAll(dbMchBaseList);
        }

        // 返回可用数据
        return merchantBaseList;
    }


    /**
     * 从文件中读取数据(新增数据)
     *
     * @param filePath 文件路径
     * @param pathType 文件类型
     * @return ResultData
     * @throws Exception
     */
    public ResultData initMerchantBase(String filePath, FilePathTypeEnum pathType, String doorPicFolder) throws Exception {

        List<MerchantBaseInfo> newMchBaseList = getNewMchFromFile(filePath, pathType, doorPicFolder);
        if (newMchBaseList.size() > 0) {
            mapper.insertByBatch(newMchBaseList);
        }

        return ResultData.getSuccessResult(newMchBaseList);
    }

    /**
     * 从文件中获取新增的商户基础信息
     *
     * @param filePath 文件地址
     * @param pathType 文件类型
     * @return List
     * @throws Exception 异常
     */
    private List<MerchantBaseInfo> getNewMchFromFile(String filePath, FilePathTypeEnum pathType, String doorPicFolder) throws Exception {
        // 检查参数
        Preconditions.checkArgument(!StringUtils.isEmpty(filePath), "文件地址为空");
        List<MerchantBaseInfo> merchantBaseList = Lists.newArrayList();

        // 从数据库中获取数据
        List<MerchantBaseInfo> dbMchList = merchantBaseInfoService.getUnusedMerchantBase();
        log.info("initMerchantBase() 从数据库中获取商户信息数=" + (dbMchList == null ? 0 : dbMchList.size()));

        // 从文件中读取数据
        List<MerchantBaseInfo> mchBaseList = null;
        // 从Excel文件中获取
        if (pathType == FilePathTypeEnum.EXCEL_FILE) {
            mchBaseList = getMerchantNameFromExcel(filePath, doorPicFolder);
        } else if (pathType == FilePathTypeEnum.DOOR_PIC_FOLDER) {
            // 从门头照文件夹中获取
            List<String> doorPicNameList = getFileNameFrom(filePath);
            mchBaseList = genMerchantBase(doorPicNameList, filePath);
        }
        log.info("getMerchantBase() 从文件[" + filePath + "]中获取商户信息数=" + (mchBaseList == null ? 0 : mchBaseList.size()));
        if (mchBaseList == null || mchBaseList.size() == 0) {
            throw new Exception("从文件中获取数据为空");
        }

        // 数据库中有没有数据
        if (CommonUtils.collectionIsEmpty(dbMchList)) {
            // 从Excel中获取数据
            merchantBaseList.addAll(mchBaseList);
        } else {
            // 数据库中商户基础信息中的名称
            List<String> dbMchNameList = Lists.newArrayList();
            dbMchList.forEach(item -> dbMchNameList.add(item.getName()));
            // 比较数据库和文件中数据差集
            List<MerchantBaseInfo> differentList = mchBaseList.stream().filter(merchantCard ->
                    !dbMchNameList.contains(merchantCard.getName())).collect(Collectors.toList());
            // 差集不为空
            merchantBaseList.addAll(differentList);
        }

        log.info("getMerchantBase() 本次新增商户信息数=" + merchantBaseList.size());
        return merchantBaseList;
    }

    /**
     * 生成随机的省市区地址
     *
     * @param merchantNameList 商户名称列表
     * @return List
     */
    private List<MerchantBaseInfo> genMerchantBase(List<String> merchantNameList, String folderPath) {
        List<MerchantBaseInfo> merchantBaseList = Lists.newArrayList();

        if (merchantNameList == null || merchantNameList.size() == 0) {
            return merchantBaseList;
        }

        for (String merchantName : merchantNameList) {
            String name = merchantName;
            if (merchantName.lastIndexOf(".") > 0) {
                name = merchantName.substring(0, merchantName.lastIndexOf("."));
            }

            // 生成四川省成都市的随机地址
            String[] addressInfo = AddressHelper.genAddressForSiChuan();
            String provinceName = addressInfo[0];
            String cityName = addressInfo[1];
            String areaName = addressInfo[2];
            String address = addressInfo[3];
            // 获取省市区对应的编号
            String provinceCode = addressHelper.getProvinceCode(provinceName);
            String cityCode = addressHelper.getCityCodeForSiChuan(cityName);
            String areaCode = addressHelper.getAreaCodeForSiChuan(areaName);

            String doorPicPath = folderPath + File.separator + merchantName;

            // 生成商户基础信息
            MerchantBaseInfo merchantBase = new MerchantBaseInfo(name, merchantName, doorPicPath, provinceName, provinceCode, cityName,
                    cityCode, areaName, areaCode, address, "UNUSED");
            // 过滤掉不能用的
            try {
                merchantBase.verify();
            } catch (Exception e) {
                log.error("解析商户基础信息异常=" + e.getMessage());
                continue;
            }
            merchantBaseList.add(merchantBase);
        }
        return merchantBaseList;
    }

    /**
     * 从Excel文件中读取商户信息
     *
     * @param excelPath Excel文件路径
     * @return List
     * @throws Exception
     */
    private List<MerchantBaseInfo> getMerchantNameFromExcel(String excelPath, String doorPicFolder) throws Exception {
        // 从文件中读取
        File file = new File(excelPath);
        if (!file.exists()) {
            throw new Exception("文件不存在: " + excelPath);
        }

        List<MerchantBaseInfo> merchantBaseList = Lists.newArrayList();

        List<Object> dataList = ExcelUtils.readExcel(excelPath, 1, 2, "ALL");
        if (dataList != null) {
            for (Object obj : dataList) {
                List<String> itemList = (List<String>) obj;
                if (StringUtils.isEmpty(itemList.get(0))) continue;

                // 商户名称、图片名称
                String name = itemList.get(1);
                String doorPicName = itemList.get(6);
                String tempName = doorPicName.substring(0, doorPicName.lastIndexOf("."));
                if (!name.equals(tempName)) {
                    name = tempName;
                }

                // 商户地址
                String provinceName = itemList.get(2);
                String cityName = itemList.get(3);
                String areaName = itemList.get(4);
                String address = itemList.get(5);
                String provinceCode = addressHelper.getProvinceCode(provinceName);
                String cityCode = addressHelper.getCityCode(provinceCode, cityName);
                String areaCode = addressHelper.getAreaCode(cityCode, areaName);

                String doorPicPath = doorPicFolder + File.separator + doorPicName;

                // 生成商户基础信息
                MerchantBaseInfo merchantBase = new MerchantBaseInfo(name, doorPicName, doorPicPath, provinceName, provinceCode, cityName,
                        cityCode, areaName, areaCode, address, "UNUSED");

                // 过滤掉不能用的
                try {
                    merchantBase.verify();
                } catch (Exception e) {
                    log.error("getMerchantNameFromExcel() 解析商户基础信息异常=" + e.getMessage());
                    continue;
                }
                merchantBaseList.add(merchantBase);
            }
        }
        return merchantBaseList;
    }

    /**
     * 从文件夹中获取文件名称
     *
     * @param doorPicFolder 文件夹路径
     * @return List
     */
    private List<String> getFileNameFrom(String doorPicFolder) {
        List<String> shopNameList = Lists.newArrayList();
        File doorPicFile = new File(doorPicFolder);
        if (!doorPicFile.exists()) {
            log.error("getFileNameFrom() 门头照图片目录为空=" + doorPicFolder);
            return shopNameList;
        }

        // 图片扩展名
        List<String> extensionNameList = Lists.newArrayList(Splitter.on(",").split("BMP,JPG,JPEG,PNG,GIF"));

        File[] doorPicFiles = doorPicFile.listFiles();
        if (doorPicFiles == null || doorPicFiles.length == 0) {
            log.error("getFileNameFrom() 门头照图片目录中没有文件=" + doorPicFolder);
            return shopNameList;
        }

        List<File> doorPicFileList = Lists.newArrayList(doorPicFiles);
        for (File item : doorPicFileList) {
            if (!item.isFile()) {
                continue;
            }
            String fileName = item.getName();
            if (fileName.lastIndexOf(".") > 0) {
                String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
                if (!extensionNameList.contains(extension.toUpperCase())) {
                    continue;
                }
            }
            // fileName = fileName.substring(0, fileName.lastIndexOf("."));
            shopNameList.add(fileName);
        }

        return shopNameList;
    }


}
