package ru.shoma.webapp.util;

/**
 * Created by Shoma on 12.05.2018.
 */
public class LazySingleton {
    volatile private static LazySingleton INSTANCE;
    static double sin;

    private LazySingleton() {
    }

    private static class LazySingletonHolder{
        private static final LazySingleton INSTANCE = new LazySingleton();
    }

    public static LazySingleton getInstance() {
        return LazySingletonHolder.INSTANCE;
//        if (INSTANCE == null) {
//            synchronized (LazySingleton.class) {
//                if (INSTANCE == null) {
//                    sin = Math.sin(13.);
//                    INSTANCE = new LazySingleton();
//                }
//            }
//        }
//        return INSTANCE;
   }
}
