package com.proxypool.secretgarden;

import com.proxypool.util.DownloadHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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
            String folderName = "F:\\test" + File.separator + DownloadHelper.getFolderName(data.getTitle());
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
                DownloadHelper.start(url, targetFile.getAbsolutePath());

                log.info("成功下载资源=" + data.getTitle() + ", " + url);
            }
            log.info("下载资源[" + data.getTitle() + "], 个数=" + num);
        }
    }




}
