package com.example.training.concurrency;

import lombok.SneakyThrows;

import java.util.LinkedList;

public class ProducerConsumer {


    private static class Buffer {
        private final LinkedList<Integer> buffer = new LinkedList<>();
        private final int capacity = 5;

        public synchronized void produce(int value) throws InterruptedException {
                while (buffer.size() == capacity) {
                    wait();
                }

                buffer.add(value);
                System.out.println("Produced: " +  value);
                notify();
        }

        public synchronized int consume() throws InterruptedException {
            while(buffer.isEmpty()) {
                wait();
            }

            var value = buffer.removeFirst();
            System.out.println("Consumed: " + value);

            notify();
            return value;
        }
    }

    public static void main(String[] args) {
        Buffer buffer = new Buffer();

        Thread produce = new Thread(() -> {

                try {

                    for(int i = 0; i < 10; i++) buffer.produce(i);

                    Thread.sleep(500);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
        });

        Thread consume = new Thread(() -> {

            try {

                for(int i = 0; i < 10; i++) buffer.consume();

                Thread.sleep(100);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });


        produce.start();
        consume.start();
    }
}
