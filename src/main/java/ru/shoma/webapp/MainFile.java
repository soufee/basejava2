package ru.shoma.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class MainFile {
    public static void main(String[] args) {
//        File file = new File(".\\.gitignore");
//        try {
//            System.out.println(file.getCanonicalPath());
//        } catch (IOException e) {
//            throw new RuntimeException("Error", e);
//        }
        File dir = new File(".\\src\\ru\\shoma\\webapp");
        //System.out.println(file.isFile());
//        String[] files = dir.list();
//        if (dir.isDirectory() && files != null) {
//            for (int i = 0; i < files.length; i++) {
//                System.out.println(files[i]);
//            }
//        }

//        try (FileInputStream fis = new FileInputStream(file.getCanonicalPath())) {
//            System.out.println(fis.read());
//        } catch (IOException e) {
//            throw new RuntimeException("Error", e);
//        }

        printDirectoryDeep(dir, "\t");
    }
//TODO красивый вывод директорий и файлов
    private static void printDirectoryDeep(File dir, String offset) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println(offset + "f:: " + file.getName());
                } else if (file.isDirectory()) {
                    System.out.println(offset+"D:: " + file.getName());
                    printDirectoryDeep(file, offset + "  ");
                }
            }
        }
    }

}
