package com.proxypool.secretgarden;

import com.google.common.collect.Lists;
import com.proxypool.service.SgDataInfoService;
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

    // 数据队列
    private BlockingQueue<SecretGardenInfo> queue;

    private SgDataInfoService sgDataInfoService;

    /**
     * 构造方法
     *
     * @param queue
     */
    public SecretGardenPicTask(BlockingQueue<SecretGardenInfo> queue, SgDataInfoService sgDataInfoService) {
        this.queue = queue;
        this.sgDataInfoService = sgDataInfoService;
    }

    @Override
    public void run() {
        // 取数据
        SecretGardenInfo data = queue.poll();
        if (data != null) {
            // 创建目录
            String folderName = "F:\\test" + File.separator + DownloadHelper.getFolderName(data.getTitle());
            File folderFile = new File(folderName);
            if (!folderFile.exists()) {
                folderFile.mkdirs();
            }

            // 资源链接
            List<String> targetUrlList = data.getTargetUrlList();
            List<Integer> successCodeList = Lists.newArrayList();

            int num = 0;
            for (String url : targetUrlList) {
                num += 1;
                // 后缀
                String extension = ".jpeg";
                if (url.lastIndexOf(".") > -1) {
                    extension = url.substring(url.lastIndexOf("."));
                }
                File targetFile = new File(folderFile, num + extension);
                // 下载
                DownloadHelper.start(url, targetFile.getAbsolutePath());
                log.info("成功下载资源=" + data.getTitle() + ", " + url);

                // 记录成功的项
                successCodeList.add(SgDataInfo.genHashCode(data.getTitle(), url));
            }
            log.info("下载资源[" + data.getTitle() + "], 个数=" + num);

            // 更新状态
            try {
                sgDataInfoService.updateStatusBatch(successCodeList);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("更新处理数据成功状态异常=" + e.getMessage());
            }
        }
    }


}
