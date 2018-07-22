package com.robberte.learning.tool.io;

import java.io.*;
import java.nio.charset.Charset;

public class FileReader {

    public static String filePath = "/Users/robberte/Work/learning-workspaces/learning-tool/temp/filewriter.log";

    public static void main(String[] args) {

        FileReader fileReader = new FileReader();
        System.out.println(fileReader.readFile(filePath));

        System.out.println(fileReader.readFile01(filePath));
    }

    public String readFile(String filePath) {
        File file = new File(filePath);
        FileInputStream fis = null;
        byte[] buffer = new byte[32];
        try {
            fis = new FileInputStream(file);
            fis.read(buffer);
            return new String(buffer, Charset.forName("utf-8"));
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public String readFile01(String filePath) {
        StringBuffer sb = new StringBuffer();
        File file = new File(filePath);
        try(java.io.FileReader fileReader = new java.io.FileReader(file);
                BufferedReader br = new BufferedReader(fileReader)) {
            String line = null;
            while((line = br.readLine()) != null) {
                sb.append(line).append("\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


}
