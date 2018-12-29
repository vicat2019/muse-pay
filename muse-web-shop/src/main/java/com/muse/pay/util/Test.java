package com.muse.pay.util;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @Author: Administrator
 * @Date: 2018 2018/7/25 11 49
 **/
public class Test {
    protected static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    public static void main(String args[]) {
        //writeConcurrencyParamFile();

        for (int i = 0; i < 100; i++) {
            System.out.println(GenRandomPersonInfo.getChineseName());
        }
    }

    public static byte[] createChecksum(String filename) throws Exception {
        InputStream fis = new FileInputStream(filename);          //将流类型字符串转换为String类型字符串

        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5"); //如果想使用SHA-1或SHA-256，则传入SHA-1,SHA-256
        int numRead;

        do {
            numRead = fis.read(buffer);    //从文件读到buffer，最多装满buffer
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);  //用读到的字节进行MD5的计算，第二个参数是偏移量
            }
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }

    public static String getMD5Checksum(String filename) throws Exception {
        byte[] b = createChecksum(filename);
        String result = "";

        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);//加0x100是因为有的b[i]的十六进制只有1位
        }
        return result;
    }

    public static String getSha1(File file) throws OutOfMemoryError, IOException, NoSuchAlgorithmException {
        MessageDigest messagedigest = MessageDigest.getInstance("SHA-1");
        FileInputStream in = new FileInputStream(file);
        FileChannel ch = in.getChannel();
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        messagedigest.update(byteBuffer);
        return bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }


    public static void writeConcurrencyParamFile() {

        String userId = "603";
        int total = 24791;

        int size = 1000;
        Set<Integer> bookSet = new HashSet<>();

        while (bookSet.size() != size) {
            Random random = new Random();
            int bookId = random.nextInt(total);
            bookSet.add(bookId);
        }

        try {
            FileOutputStream out = new FileOutputStream(new File("D:\\data3.txt"), true);
            OutputStreamWriter writer = new OutputStreamWriter(out);

            Iterator<Integer> it = bookSet.iterator();
            while (it.hasNext()) {
                int bookId = it.next();

                String content = userId + "," + bookId;
                out.write(content.getBytes("utf-8"));
                String newLine = System.getProperty("line.separator");
                out.write(newLine.getBytes());
            }

            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
