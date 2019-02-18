package com.proxypool.secretgarden;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * @program: muse-pay
 * @description: SecretGarden数据处理
 * @author: Vincent
 * @create: 2019-02-18 15:24
 **/
public class SecretGardenPicTask implements Runnable {
    private Logger log = LoggerFactory.getLogger("SecretGardenPicTask");

    private BlockingQueue<SecretGardenInfo> queue;

    /**
     * 构造方法
     *
     * @param queue
     */
    public SecretGardenPicTask(BlockingQueue<SecretGardenInfo> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {

        SecretGardenInfo data = queue.poll();
        if (data != null) {
            // 创建目录
            String folderName = "F:\\test" + File.separator + data.getTitle();
            File folderFile = new File(folderName);
            if (!folderFile.exists()) {
                folderFile.mkdirs();
            }

            List<String> targetUrlList = data.getTargetUrlList();

            int num = 0;
            for (String url : targetUrlList) {
                num += 1;
                // 后缀
                String extension = ".jpeg";
                if (url.lastIndexOf(".") > -1) {
                    extension = url.substring(url.lastIndexOf("."));
                }
                File targetFile = new File(folderFile, num + extension);
                downloadSource(url, targetFile.getAbsolutePath());
            }
            log.info("下载资源[" + data.getTitle() + "], 个数=" + num);
        }
    }

    /**
     * 下载资源
     *
     * @param sourceUrl
     * @param fileName
     */
    private void downloadSource(String sourceUrl, String fileName) {
        try {
            URL url = new URL(sourceUrl);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            FileOutputStream fileOutputStream = new FileOutputStream(new File(fileName));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            byte[] context = output.toByteArray();
            fileOutputStream.write(context);
            dataInputStream.close();
            fileOutputStream.close();

            log.info("成功下载资源=" + fileName + ", " + sourceUrl);

        } catch (IOException e) {
            e.printStackTrace();
            log.error("下载资源异常[" + sourceUrl + "]" + e.getMessage());
        }
    }


}
