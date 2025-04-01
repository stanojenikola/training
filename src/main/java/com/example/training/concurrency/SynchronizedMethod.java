package com.example.training.concurrency;

public class SynchronizedMethod {

    private static class Counter {
        private int count = 0;

        public synchronized void increment() {
            count++;
        }

        public int getCount() {
            return count;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i <  1000; i++) {
                counter.increment();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i <  1000; i++) {
                counter.increment();
            }
        });

        thread1.start();
        thread1.join();

        thread2.start();
        thread2.join();

        // without join the print will be executed before second thread execution and correct count will not be printed
        System.out.println(counter.getCount());
    }
}
