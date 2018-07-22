package com.robberte.learning.tool.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class FileWriter {

    public static String filePath = "/Users/robberte/Work/learning-workspaces/learning-tool/temp/filewriter.log";

    public static void main(String[] args) {

        //String filePath = "/Users/robberte/Work/learning-workspaces/learning-tool/temp/filewriter.log";
        String content = "Hello World!";
        FileWriter fileWriter = new FileWriter();
        fileWriter.writerFile(filePath, content);

        fileWriter.writerFile01(filePath, content + "02");
    }

    public void writerFile(String fileName, String content) {
        File file = new File(fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(content.getBytes(Charset.forName("utf-8")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void writerFile01(String fileName, String content) {
        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
