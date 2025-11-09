package com.urise.util;

public class DeadLock {
    private static boolean b;

    static Thread thread0 = new Thread() {
        @Override
        public void run() {
            System.out.println("thread 0 is running");
            if (!b) {
                try {
                    thread1.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                b = true;
                System.out.println("thread 0 is over");
            }
        }
    };
    static Thread thread1 = new Thread() {
        @Override
        public void run() {
            System.out.println("thread 1 is running");
            if (!b) {
                try {
                    thread0.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            b = true;
            System.out.println("thread 1 is over");
        }
    };

    public static void main(String[] args) throws InterruptedException {

        thread0.start();
        thread1.start();
    }
}
