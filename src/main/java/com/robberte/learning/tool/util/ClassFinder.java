package com.robberte.learning.tool.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author robberte
 * @date 2018/7/21 下午2:20
 */
public class ClassFinder {

    /**
     * 以文件的形式来获取包下的所有class
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     */
    public static List<Class> findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive) {
        List<Class> classes = new ArrayList<Class>();
        File dir = new File(packagePath);
        if(!dir.exists() || !dir.isAbsolute()) {
            // todo add warning log
            return classes;
        }

        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则，如果可以循环（包含子目录） 或者 是以 .class结尾的文件（编译后的java类文件）
            public boolean accept(File file) {
                return (file.isDirectory() && recursive)
                        || file.getName().endsWith(".class")
                        || file.getName().endsWith(".java");
            }
        });

        // 循环所有文件
        for(File file : dirfiles) {
            String fileName = file.getName();
            // 如果是目录，则递归调用扫描
            if(file.isDirectory()) {
                classes.addAll(findAndAddClassesInPackageByFile(packageName
                        + "." + fileName, file.getAbsolutePath(), recursive));
                continue;
            }
            String className = "";
            if(fileName.endsWith(".class")) {
                className = fileName.substring(0, fileName.length() - 6);
            } else if(fileName.endsWith(".java")) {
                className = fileName.substring(0, fileName.length() - 5);
            }

            try {
                classes.add(ClassLoader.getSystemClassLoader().loadClass(packageName + "." + className));
            } catch(ClassNotFoundException e) {
                //e.printStackTrace();
            }
        }
        return classes;
    }
}
