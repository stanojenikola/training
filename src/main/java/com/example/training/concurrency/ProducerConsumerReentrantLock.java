package com.example.training.concurrency;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ProducerConsumerReentrantLock {

    private static class Buffer {
        private final LinkedList<Integer> linkedList = new LinkedList<>();
        private final int capacity = 5;
        private final Lock lock = new java.util.concurrent.locks.ReentrantLock();
        private final Condition notFull = lock.newCondition();
        private final Condition notEmpty = lock.newCondition();

        public void produce(int num) throws InterruptedException {
            lock.lock();

            try {

                while(linkedList.size() == capacity) {
                    notFull.await(); // wait
                }

                linkedList.add(num);
                System.out.println("Produced: " + num);
                notEmpty.signal(); // send a signal, a list is not empty
            } finally {
                lock.unlock();
            }
        }

        public int consumer() throws InterruptedException {
            lock.lock();

            try {
                while (linkedList.isEmpty()) {
                    notEmpty.await();
                }

                int value = linkedList.removeFirst();
                System.out.println("Consume: " + value);

                notFull.signal();

                return value;
            } finally {
                lock.unlock();
            }
        }

        public static void main(String[] args) {
            Buffer buffer = new Buffer();

            var thread1 = new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    try {
                        buffer.produce(i);
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            var thread2 = new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    try {
                        buffer.consumer();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            thread1.start();
            thread2.start();
        }
    }
}
