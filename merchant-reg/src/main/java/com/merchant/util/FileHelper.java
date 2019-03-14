package com.merchant.util;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.google.common.base.Preconditions;
import com.merchant.entity.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @Description: 模拟表单上传文件
 * @Author: Vincent
 * @Date: 2019/2/16
 */
public class FileHelper {
    private static Logger log = LoggerFactory.getLogger("FileHelper");

    // BUFFER大小
    private static final int BUFFER = 2048;

    // 文件后缀名
    public static final String[] FILE_EXTENSIONS = {"xlsx", "xls", "zip"};

    /**
     * 根据名称获取门头照文件
     *
     * @param doorPicFilePath 门头照文件路径
     * @return File
     */
    public static File getDoorPicFile(String doorPicFilePath) {
        if (StringUtils.isEmpty(doorPicFilePath)) {
            log.error("获取门头照图片文件异常，参数不能为空");
            return null;
        }
        return new File(doorPicFilePath);
    }


    /**
     * 移动已经处理的图片到其他目录
     *
     * @param srcFile    移动的文件
     * @param destFolder 目的目录
     * @return boolean
     * @throws Exception 异常
     */
    public static boolean removeDoneFile(File srcFile, String destFolder) throws Exception {
        String destFilePath = destFolder.substring(0, destFolder.lastIndexOf(File.separator)) + File.separator +
                destFolder.substring(destFolder.lastIndexOf(File.separator) + 1) + "_DONE";
        return moveFile(srcFile, destFilePath);
    }


    /**
     * 移动文件
     *
     * @param srcFile    移动的文件
     * @param destFolder 目的目录
     * @return boolean
     * @throws Exception 异常
     */
    public static boolean moveFile(File srcFile, String destFolder) throws Exception {
        // 检查参数
        Preconditions.checkArgument(srcFile != null && srcFile.exists(), "文件不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(destFolder), "目标文件夹路径不能为空");

        String fileName = srcFile.getName();

        File destFile = new File(destFolder);
        if (!destFile.exists()) {
            destFile.mkdirs();
        }

        if (srcFile.renameTo(new File(destFile + File.separator + fileName))) {
            return true;
        } else {
            log.error("moveFile() 移动文件失败=" + srcFile.getAbsolutePath() + ", 目标地址=" + destFolder);
            return false;
        }
    }

    /**
     * 上传文件
     *
     * @param uploadFile 要上传的文件
     * @return String
     */
    public static String uploadFile(File uploadFile) {
        String doorPicUrl = "";
        String urlStr = "http://www.ruishengglass.cn//api-v1-user/upload.html";
        String content = FileHelper.uplad(uploadFile.getAbsolutePath(), urlStr);
        if (StringUtils.isEmpty(content)) {
            log.error("uploadFile() 上传图片，返回内容为空");
            return null;
        }
        JSONObject json = JSONObject.parseObject(content);
        if (json.getInteger("code") == 1) {
            JSONObject data = json.getJSONObject("data");
            if (data != null) {
                doorPicUrl = data.getString("file");
            }
        }
        if (StringUtils.isEmpty(doorPicUrl)) {
            log.error("uploadFile() 上传图片，返回图片地址为空=" + uploadFile.getName());
            return null;
        }
        log.info("uploadFile() 上传图片=" + uploadFile.getAbsolutePath() + ", url=" + doorPicUrl);

        return doorPicUrl;
    }

    /**
     * 上传图片
     *
     * @param filePath 图片地址
     * @param urlStr   上传URL
     * @return String
     */
    private static String uplad(String filePath, String urlStr) {
        Map<String, String> textMap = new HashMap<>();
        textMap.put("name", "testname");
        Map<String, String> fileMap = new HashMap<>();
        fileMap.put("file", filePath);
        String uploadResult = formUpload(urlStr, textMap, fileMap);

        log.info("上传图片返回结果=" + uploadResult);
        return uploadResult;
    }

    /**
     * 上传图片
     *
     * @param urlStr  上传地址
     * @param textMap 参数
     * @param fileMap 文件
     * @return String
     */
    private static String formUpload(String urlStr, Map<String, String> textMap,
                                     Map<String, String> fileMap) {
        String res = "";
        HttpURLConnection conn = null;
        String BOUNDARY = "---------------------------123821742118716"; //boundary就是request头和上传文件内容的分隔符
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());

            // 文本
            if (textMap != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator iterator = textMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
                    strBuf.append(inputValue);
                }
                out.write(strBuf.toString().getBytes());
            }

            // 文件
            if (fileMap != null) {
                Iterator fileIterator = fileMap.entrySet().iterator();
                while (fileIterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) fileIterator.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    File file = new File(inputValue);
                    String filename = file.getName();
                    String contentType = new MimetypesFileTypeMap()
                            .getContentType(file);
                    if (filename.endsWith(".png")) {
                        contentType = "image/png";
                    }
                    if (contentType == null || contentType.equals("")) {
                        contentType = "application/octet-stream";
                    }
                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

                    out.write(strBuf.toString().getBytes());
                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
            }

            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传图片异常=" + e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return res;
    }

    /**
     * 处理上传文件
     *
     * @param file             上传的文件
     * @param uploadFolderPath 上传的文件
     * @return ResultData
     */
    public static ResultData<List<File>> uploadAndGetSubFileList(MultipartFile file, String uploadFolderPath) throws Exception {
        // 上传文件，获取上传后的文件路径
        ResultData<String> uploadResult = doUploadFile(file, uploadFolderPath);
        if (!uploadResult.whetherOk()) {
            return ResultData.getErrResult(uploadResult.getMessage());
        }
        String filePath = uploadResult.getData();
        if (StringUtils.isEmpty(filePath)) {
            throw new Exception("上传文件后返回文件地址为空");
        }

        // 加压文件获取子文件列表
        List<File> dataFileList = getSubFile(file.getOriginalFilename(), filePath);

        log.info("处理后上传的文件个数=" + dataFileList.size());
        return ResultData.getSuccessResult(dataFileList);
    }


    /**
     * 解压后获取文件夹下的子文件
     *
     * @param originalFileName
     * @param filePath
     * @return
     * @throws Exception
     */
    private static List<File> getSubFile(String originalFileName, String filePath) throws Exception {
        // 上传文件列表
        List<File> dataFileList = new ArrayList<>();

        // 如果是压缩包
        if (!StringUtils.isEmpty(originalFileName) && isZipFile(originalFileName)) {
            // 解压
            String unzipFolderPath = unzipFile(filePath);
            if (StringUtils.isEmpty(unzipFolderPath)) {
                throw new Exception("返回解压后的文件目录为空");
            }

            // 遍历文件
            File unzipFolder = new File(unzipFolderPath);
            if (!unzipFolder.exists()) {
                throw new Exception("解压后的目录文件不存在[" + unzipFolderPath + "]");
            }
            File[] fileAr = unzipFolder.listFiles();
            if (fileAr == null) {
                throw new Exception("解压后目录下的文件为空");
            }
            for (File item : fileAr) {
                if (item != null) dataFileList.add(item);
            }
        } else {
            dataFileList.add(new File(filePath));
        }
        return dataFileList;
    }

    /**
     * 处理上传文件
     *
     * @param uploadFile 上传的文件
     * @return ResultData
     */
    public static ResultData<String> doUploadFile(MultipartFile uploadFile, String uploadFolderPath) throws Exception {
        // 检查参数
        Preconditions.checkArgument(uploadFile != null, "上传文件为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(uploadFolderPath), "保存上传文件目录为空");

        // 获取扩展名称
        String originalFileName = uploadFile.getOriginalFilename();
        if (StringUtils.isEmpty(originalFileName)) {
            return ResultData.getErrResult("获取上传文件名称失败");
        }
        Integer index = originalFileName.lastIndexOf(".");
        if (index < 0) {
            return ResultData.getErrResult("文件类型异常");
        }
        // 检查文件类型
        String extension = originalFileName.substring(index + 1);
        if (!isContainExtension(extension)) {
            return ResultData.getErrResult("文件类型[" + extension + "]异常");
        }

        // 重新生成唯一文件名
        String newFileName = UUID.randomUUID().toString() + "." + extension;
        log.info("上传文件，原始名称= " + originalFileName + ", 新名称=" + newFileName);

        // 检查路径是否存在，不存在，则创建
        File destFolder = new File(uploadFolderPath);
        if (!destFolder.exists()) {
            if (!destFolder.mkdirs()) {
                return ResultData.getErrResult("创建上传文件目录失败");
            }
            log.info("创建目录=" + uploadFolderPath);
        }

        // 创建文件
        File destFile = new File(uploadFolderPath + File.separator + newFileName);
        uploadFile.transferTo(destFile);

        // 返回上传后的文件名称
        ResultData<String> result = ResultData.getSuccessResult();
        result.setData(destFile.getPath());
        return result;
    }

    /**
     * 是否是包含该扩展
     *
     * @param extension
     * @return
     */
    public static boolean isContainExtension(String extension) {
        for (String item : FILE_EXTENSIONS) {
            if (item.equals(extension.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查文件是否是Excel文件
     *
     * @param fileName 文件名称
     * @return boolean
     */
    public static boolean isExcelFile(String fileName) {
        if (StringUtils.isEmpty(fileName)) return false;
        if (fileName.lastIndexOf(".") < 0) return false;

        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
        if ("XLSX".equals(extension) || "XLS".equals(extension)) {
            return true;
        }

        return false;
    }

    /**
     * 是否是压缩文件
     *
     * @param filePath 文件路径
     * @return
     */
    public static boolean isZipFile(String filePath) {
        int index = filePath.lastIndexOf(".");
        if (index > 0) {
            String extension = filePath.substring(index + 1);
            return "zip".equals(extension.trim().toLowerCase());
        }

        return false;
    }

    /**
     * 解压文件
     *
     * @param srcFilePath
     */
    public static String unzipFile(String srcFilePath) throws Exception {
        // 检查参数
        Preconditions.checkArgument(!StringUtils.isEmpty(srcFilePath), "解压文件夹，文件路径参数为空");

        ZipFile zipFile = new ZipFile(srcFilePath, Charset.forName("GBK"));
        String zipName = srcFilePath.substring(0, srcFilePath.lastIndexOf("."));
        // 根据压缩包文件名称创建目录
        File rootFolder = new File(zipName);
        rootFolder.deleteOnExit();
        rootFolder.mkdirs();

        int count = 0;
        Enumeration enumeration = zipFile.entries();
        while (enumeration.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();

            // 压缩像为目录，创建
            if (zipEntry.isDirectory()) {
                new File(zipName + "/" + zipEntry.getName()).mkdirs();
                continue;
            }

            // 获取压缩项流
            BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(zipEntry));

            // 生成压缩项文件，如果父目录不存在，则创建
            File file = new File(zipName + "/" + zipEntry.getName());
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            // 输出文件
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file), BUFFER);
            byte[] data = new byte[BUFFER];
            while ((bis.read(data, 0, BUFFER)) != -1) {
                bos.write(data, 0, BUFFER);
            }

            // 关闭流
            bos.flush();
            bos.close();
            bis.close();
            count++;
        }
        log.info("解压文件[" + srcFilePath + "], 子文件个数=" + count);
        return zipName;
    }

    /**
     * 获取目录下文件
     *
     * @param folderPath 目录路径
     * @return
     * @throws Exception
     */
    public static List<File> getSubFileList(String folderPath) throws Exception {
        Preconditions.checkArgument(!StringUtils.isEmpty(folderPath), "目录参数不能为空");
        return getSubFileList(new File(folderPath));
    }

    /**
     * 获取目录下文件
     *
     * @param folderFile 目录路径
     * @return
     * @throws Exception
     */
    public static List<File> getSubFileList(File folderFile) throws Exception {
        Preconditions.checkArgument(folderFile != null, "目录参数不能为空");

        if (!folderFile.exists()) throw new Exception("目录地址不存在=" + folderFile.getAbsolutePath());

        File[] subFileAr = folderFile.listFiles();
        if (subFileAr == null || subFileAr.length == 0) throw new Exception("文件夹内容为空");

        return Lists.newArrayList(subFileAr);
    }


    public static void main(String[] args) {
        String filePath = "C:\\Users\\Administrator\\Desktop\\滨农进件资料\\张旭良 二类卡\\3.jpg";
        String urlStr = "http://www.ruishengglass.cn//api-v1-user/upload.html";

        uplad(filePath, urlStr);
    }


}
