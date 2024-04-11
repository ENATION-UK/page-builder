package com.enation.itbuilder.image2code.api;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/

public class FileUtils {

    /**
     * 读取文件内容
     * @param fileName 相对于resources下的路径
     * @return 文件内容
     */
    public static String readFile(String fileName) {
        // 使用类加载器获取文件输入流
        InputStream is = FileUtils.class.getResourceAsStream(fileName);
        if (is == null) {
            System.out.println("文件未找到: " + fileName);
            return null;
        }

        // 使用StringBuilder来存储文件内容
        StringBuilder content = new StringBuilder();

        // 使用BufferedReader读取文件内容
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }

}
