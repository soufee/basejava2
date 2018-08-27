package ru.shoma.webapp;

import ru.shoma.webapp.model.Resume;
import ru.shoma.webapp.storage.*;
import ru.shoma.webapp.storage.Storage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainArray {
    private final static Storage ARRAY_STORAGE = new MapStorage();

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Resume r;
        while (true) {
            System.out.print("Введите одну из команд - (list | save fullName | delete uuid | get uuid | update uuid fullName | clear | exit): ");
            String[] params = reader.readLine().trim().toLowerCase().split(" ");
            if (params.length < 1 || params.length > 3) {
                System.out.println("Неверная команда.");
                continue;
            }
            String param = null;
            if (params.length >1) {
                param = params[1].intern();
            }
            switch (params[0]) {
                case "list":
                    printAll();
                    break;
                case "size":
                    System.out.println(ARRAY_STORAGE.size());
                    break;
                case "save":
                    if (params.length != 2) {
                        System.out.println("Для сохранения новой записи после слова size нужно ввести идентификатор");
                        break;
                    } else {
                        r = new Resume(param);
                        ARRAY_STORAGE.save(r);
                        printAll();
                        break;
                    }
                case "delete":
                    if (params.length != 2) {
                        System.out.println("Для удаления записи после слова delete нужно ввести идентификатор");
                        break;
                    } else {
                        ARRAY_STORAGE.delete(param);
                        printAll();
                        break;
                    }
                case "get":
                    if (params.length != 2) {
                        System.out.println("Для получения записи после слова get нужно ввести идентификатор");
                        break;
                    } else {
                        System.out.println(ARRAY_STORAGE.get(param));
                        break;
                    }

                case "update":
                    if (params.length != 3) {
                        System.out.println("Для обновления записи после слова update нужно ввести идентификатор");
                        break;
                    } else {
                        r = new Resume(param, params[2]);
                        ARRAY_STORAGE.update(r);
                        printAll();
                        break;
                    }
                case "clear":
                    ARRAY_STORAGE.clear();
                    printAll();
                    break;
                case "exit":
                    return;
                default:
                    System.out.println("Неверная команда.");
                    break;
            }
        }
    }

    private static void printAll() {
        Resume[] all = ARRAY_STORAGE.getAllSorted().toArray(new Resume[ARRAY_STORAGE.size()]);
        System.out.println("----------------------------");
        if (all.length == 0) {
            System.out.println("Empty");
        } else {
            for (Resume r : all) {
                System.out.println(r);
            }
        }
        System.out.println("----------------------------");
    }
}