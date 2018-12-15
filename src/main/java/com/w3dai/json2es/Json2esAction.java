package com.w3dai.json2es;

import java.io.*;

public class Json2esAction {
    public void readJasonAndWriteToES(){
        //Read the file of "dataSet01.txt" and write to es
        String filePath =  "./dataSet01.txt";
        try {
            File file = new File(filePath);
            if(file.isFile() && file.exists()) {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String lineTxt;
                while ((lineTxt = br.readLine()) != null) {
                    System.out.println(lineTxt);
                    //Plan to write to ES here
                }
                br.close();
            } else {
                System.out.println("文件不存在!");
            }
        } catch (Exception e) {
            System.out.println("文件读取错误!");
        }
    }


}
