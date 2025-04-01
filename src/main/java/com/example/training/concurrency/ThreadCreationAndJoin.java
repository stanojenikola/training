package com.example.training.concurrency;

public class ThreadCreationAndJoin {

    private static class NumberPrinter implements Runnable {

        private final int limit;

        public NumberPrinter(int limit) {
            this.limit = limit;
        }

        @Override
        public void run() {
            System.out.println();
            var threadName = Thread.currentThread().getName();

            for (int i = 0; i < limit; i++) {
                System.out.println("Printing number " +  i + " for thread: " + threadName);
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(new NumberPrinter(8), "thread-1");

        Thread thread2 = new Thread(new NumberPrinter(233),"thread-2");
        thread1.start();
        //TODO: after adding thread.join first thread will finish EXECUTION till the end.
        //thread1.join();
        thread2.start();
    }
}
