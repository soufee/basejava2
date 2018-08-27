package ru.shoma.webapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class MainConcurrency {
    public static final int THREADS_NUMBER = 10000;
    private static int counter;
    private static AtomicInteger atomicInteger = new AtomicInteger();
    //  private static final Object LOCK = new Object();
    private static final Lock LOCK = new ReentrantLock();
    private static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT = ThreadLocal.withInitial(SimpleDateFormat::new);

    public static void main(String[] args) throws InterruptedException {
//        System.out.println(Thread.currentThread().getName());
//        Thread thread0 = new Thread(() -> {
//            for (int i = 0; i < 10; i++) {
//                System.out.println(i + " с первого");
//            }
//        });
//        Thread thread1 = new Thread(() -> {
//            for (int i = 0; i < 10; i++) {
//                System.out.println(i + " со второго");
//                           }
//        });
//        thread0.start();
//        thread1.start();

        CountDownLatch latch = new CountDownLatch(THREADS_NUMBER);
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        //      CompletionService completionService = new ExecutorCompletionService(executorService);
        //   List<Thread> threads = new ArrayList<>(THREADS_NUMBER);
        for (int i = 0; i < THREADS_NUMBER; i++) {
            Future<Integer> future = executorService.submit(() ->

//            Thread thread = new Thread(() ->
            {
                for (int j = 0; j < 100; j++) {
                    inc();
                    atomicInteger.incrementAndGet();
                    System.out.println(SIMPLE_DATE_FORMAT.get().format(new Date()));
                }
                latch.countDown();
                return counter;
            });
//            thread.start();
            // threads.add(thread);
            //           completionService.poll();
        }
//        threads.forEach(t -> {
//            try {
//                t.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
        latch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();
        System.out.println(counter);
        System.out.println(atomicInteger);
    }

    private static void inc() {
//        LOCK.lock();
//        try {
            counter++;
//        } finally {
//            LOCK.unlock();
//        }

    }

}
