package ru.shoma.webapp;

import ru.shoma.webapp.storage.SqlStorage;
import ru.shoma.webapp.storage.Storage;

import java.io.*;
import java.util.Properties;

public class Config {
    private static final File PROPS = new File (getHomeDir(), "src/main/java/ru/shoma/webapp/config/resumes.properties");
    private static final Config INSTANCE = new Config();
    private final File storageDir;
    private Storage storage;

    public static Config getInstance() {
        return INSTANCE;
    }

    public File getStorageDir() {
        return storageDir;
    }

    public Storage getStorage() {
        return storage;
    }

    public static File getHomeDir(){
      String prop=  System.getProperty("homeDir");
      File homeDir = new File(prop==null?".":prop);
      if (!homeDir.isDirectory()){
        throw new IllegalStateException(homeDir+" is not Directory");
      }
      return homeDir;
    }

    private Config() {
         try (InputStream is = new FileInputStream(PROPS)) {
             try {
                 Class.forName("org.postgresql.Driver");
             } catch (ClassNotFoundException e) {
                 e.printStackTrace();
             }
             Properties props = new Properties();
            props.load(is);
            storageDir = new File(props.getProperty("storage.dir"));
            storage = new SqlStorage(props.getProperty("db.url"), props.getProperty("db.user"), props.getProperty("db.password"));
            System.out.println("Storage created "+storage);
        } catch (IOException e) {
            throw new IllegalStateException("Invalid ru.shoma.webapp.config file " + PROPS.getAbsolutePath());
        }
    }
}
