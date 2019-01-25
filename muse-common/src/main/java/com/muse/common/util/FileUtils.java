package com.muse.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

/**
 * 文件操作工具
 */
public class FileUtils {

    /**
     * 向文件中写入内容集合
     *
     * @param filePath    文件路径
     * @param contentList 内容集合
     */
    public static void writeContentToFile(String filePath, Set<String> contentList) {
        try {
            FileOutputStream out = new FileOutputStream(new File(filePath), true);
            for (String content : contentList) {
                out.write(content.getBytes("utf-8"));
                out.write(System.getProperty("line.separator").getBytes());
            }
            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
