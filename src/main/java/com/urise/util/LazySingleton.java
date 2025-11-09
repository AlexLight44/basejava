package com.urise.util;

public class LazySingleton {
    int i;
    volatile private static LazySingleton INSTANCE;
    double sin = Math.sin(13);
    private LazySingleton() {
    }

    private static class LazySingletonHolder{
        private static final LazySingleton INSTANCE = new LazySingleton();
    }


    public static LazySingleton getInstance() {
        return LazySingletonHolder.INSTANCE;
    }

}
