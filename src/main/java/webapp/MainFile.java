package main.java.webapp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainFile {
    static String directory = "C:\\projects\\basejava\\src";
    static File file = new File(directory);
    static List<String> list = new ArrayList<>();

    public static List<String> directories(File dir) {
        if (file.isDirectory()) {
            list.add(dir.getName());
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f : files) {
                    directories(f);
                }
            }
        }
        return list;
    }

    public static void main(String[] args) {
        List<String> directories = directories(file);
        directories.forEach(System.out::println);

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
