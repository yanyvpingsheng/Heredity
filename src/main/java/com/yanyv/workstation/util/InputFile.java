package com.yanyv.workstation.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.*;

// 文件输入类
public class InputFile {

    // 传入文件 返回车间模型
    public static WorkStation load(File file) {
        // 从文件读取实际json数据
        String data = loadData(file);
        //System.out.println(data);
        JSONObject json = new JSONObject(data);
        //System.out.println(json);

        // 调用车间模型类的静态工厂方法生成车间模型并返回
        return WorkStation.load(json);
    }

    // 传入文件 返回字符串 文件内容
    private static String loadData(File file) {
        String data = "";
        BufferedReader reader = null;
        try {
            // 以行为单位读取文件内容，一次读一整行：
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                //System.out.println("line : " + tempString);
                data += tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
