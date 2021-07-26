package com.example.filetransreader.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author fenng 2021-05-24 15:04
 */
public class FileUtil {

    /**
     * 左填充
     *
     * @param str    基础字符
     * @param length 长度
     * @param ch     填充内容
     */
    public static String leftPad(String str, int length, char ch) {
        if (str.length() >= length) {
            return str;
        }
        char[] chs = new char[length];
        Arrays.fill(chs, ch);
        char[] src = str.toCharArray();
        System.arraycopy(src, 0, chs, length - src.length, src.length);
        return new String(chs);

    }

    /**
     * 获取路径下的所有文件/文件夹
     *
     * @param directoryPath  需要遍历的文件夹路径
     * @param isAddDirectory 是否将子文件夹的路径也添加到list集合中
     * @return
     */
    public static List<String> getAllFile(String directoryPath, boolean isAddDirectory) {
        List<String> list = new ArrayList<String>();
        File baseFile = new File(directoryPath);
        if (baseFile.isFile() || !baseFile.exists()) {
            return list;
        }
        File[] files = baseFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if (isAddDirectory) {
                    list.add(file.getAbsolutePath());
                }
                list.addAll(getAllFile(file.getAbsolutePath(), isAddDirectory));
            } else {
                list.add(file.getAbsolutePath());
            }
        }
        return list;
    }

    /**
     * 删除文件
     *
     * @param fileName 待删除的完整文件名
     * @return result 是否删除成功
     */
    public static boolean delete(String fileName) {
        boolean result = false;
        File f = new File(fileName);
        if (f.exists()) {
            result = f.delete();

        } else {
            result = true;
        }
        return result;
    }

    /**
     * 读取文件内容
     *
     * @param fileName 待读取的完整文件名
     * @return 文件内容
     */
    public static String read(String fileName) throws IOException {
        File f = new File(fileName);
        FileInputStream fs = new FileInputStream(f);
        String result = null;
        byte[] b = new byte[fs.available()];
        fs.read(b);
        fs.close();
        result = new String(b);
        return result;
    }

    /**
     * 写文件
     *
     * @param fileName    目标文件名
     * @param fileContent 写入的内容
     */
    public static boolean write(String fileName, String fileContent) throws IOException {
        boolean result = false;
        File f = new File(fileName);
        FileOutputStream fs = new FileOutputStream(f);
        byte[] b = fileContent.getBytes();
        fs.write(b);
        fs.flush();
        fs.close();
        result = true;
        return result;
    }

    /**
     * 追加内容到指定文件
     *
     * @param fileName    文件名称
     * @param fileContent 文件内容
     */
    public static boolean append(String fileName, String fileContent) throws IOException {
        boolean result = false;
        File f = new File(fileName);
        if (f.exists()) {
            RandomAccessFile rFile = new RandomAccessFile(f, "rw");
            byte[] b = fileContent.getBytes();
            long originLen = f.length();
            rFile.setLength(originLen + b.length);
            rFile.seek(originLen);
            rFile.write(b);
            rFile.close();
        }
        result = true;
        return result;
    }

    public static void main(String[] args) throws IOException {

//        shard();
//        merge();

        System.out.println(CrcUtils.getCRC32("C:\\testDir\\intemp\\NEST培训视频.zip"));
        //  System.out.println(CrcUtils.getCRC32("C:\\testDir\\outtemp\\bigFileTemp\\123.zip"));

    }

    private static void shard() throws IOException {
        File file = new File("C:\\testDir\\intemp\\NEST培训视频.zip");
        RandomAccessFile rw = new RandomAccessFile(file, "rw");
        int sub = 2097152;
        long length = file.length();
        System.out.println("nest文件大小" + length);
        long count = length / sub;
        long po = length % sub;
        if (po > 0) {
            count++;
        }
        int countLen = (count + "").length();
        String outPath = "C:\\testDir\\99988\\";
        String name = file.getName();
        for (int i = 1; i <= count; i++) {
            int startPos = (i - 1) * sub;
            byte[] b = new byte[sub];

            if (po > 0 && i == count) {
                b = new byte[(int) po];
            }

            rw.seek(startPos);
            rw.read(b);
            String fileName = name + FileUtil.leftPad((i) + "", countLen, '0') + ".part";
            FileOutputStream fs = new FileOutputStream(outPath + fileName);
            fs.write(b);
            fs.flush();
            fs.close();
        }
        rw.close();
    }

    private static void merge() throws IOException {
        File file = new File("C:\\testDir\\99988");
        File[] files = file.listFiles();
        int pos;
        for (File f : files) {
            RandomAccessFile rw = new RandomAccessFile(f, "rw");
            RandomAccessFile ww = new RandomAccessFile("C:\\testDir\\outtemp\\bigFileTemp\\123.zip", "rw");

            long length1 = rw.length();
            byte[] bytes = new byte[(int) length1];
            rw.read(bytes);

            String name = f.getName();
            String numStr = name.substring(name.lastIndexOf(".") - 3, name.lastIndexOf("."));
            Integer sortid = Integer.valueOf(numStr);

            pos = (sortid - 1) * 2097152;

            ww.seek(pos);
            ww.write(bytes);

            rw.close();
            ww.close();
        }
    }

    //    RandomAccessFile raf = new RandomAccessFile("C:\\testDir\\112233.txt", "rw");
//    RandomAccessFile rww = new RandomAccessFile("C:\\testDir\\2233.txt", "rw");
//    long length = raf.length();
//
//    int startpos;
//    int sub = 2;
//
//        raf.seek(0);
//    byte[] b = new byte[2];
//        raf.read(b);
//
//        rww.seek(0);
//        rww.write(b);
//
//
//    //        for (int i = 0; i < 5; i++) {
//    //            byte[] b = new byte[2];
//    //            raf.seek((i) * sub);
//    //            raf.read(b);
//    //
//    //            rww.seek((i) * sub);
//    //            rww.write(b);
//    //        }
//
//
//        raf.close();
//        rww.close();
}