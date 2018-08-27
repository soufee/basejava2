package ru.shoma.webapp;

public class MyDeadLock {

    public static void main(String[] args) {
        final Object obj1 = new Object();
        final Object obj2 = new Object();

        new Locker(obj1, obj2).start();
        new Locker(obj2, obj1).start();
    }

    static class Locker extends Thread {
        private final Object obj1;
        private final Object obj2;

        Locker(Object obj1, Object obj2) {
            this.obj1 = obj1;
            this.obj2 = obj2;
        }

        @Override
        public void run() {
            synchronized (obj1) {
                try {
                    System.out.println("Catched the " + obj1 + " by " + Thread.currentThread().getName());
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
                synchronized (obj2) {
                    System.out.println("Catched the " + obj2 + " by " + Thread.currentThread().getName());
                }
            }
        }
    }
}