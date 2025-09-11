package main.java.webapp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainFile {
    static String directory = "C:\\projects\\basejava\\src";
    static List<String> list = new ArrayList<>();

    public static void directories(File directory, int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print("  ");
        }
        System.out.println(directory.getName());
        if (directory.isDirectory()) {
            list.add(directory.getName());
            File[] files = directory.listFiles();
            if (files != null) {
                for (File f : files) {
                    directories(f, depth + 1);
                }
            }
        }
    }

    public static void main(String[] args) {
        File file = new File(directory);
        directories(file, 0);
//        String filePath = "C:\\projects\\basejava";
//        File file = new File(filePath);
//        try {
//            System.out.println(file.getCanonicalPath());
//        } catch (IOException e) {
//            throw new RuntimeException("Error", e);
//        }
//        File dir = new File(filePath);
//        System.out.println(dir.isDirectory());
//        String[] list = dir.list();
//        if (list != null) {
//            for (String name : list) {
//                System.out.println(name);
//            }
//        }
//
//        try(FileInputStream fis = new FileInputStream(filePath)) {
//            System.out.println(fis.read());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}
