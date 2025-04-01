package com.example.training.concurrency;

public class Deadlock {


    private static class Resource {
        private final String name;

        Resource(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }


    public static void main(String[] args) {
        Resource resource1 = new Resource("resource1");
        Resource resource2 = new Resource("resource2");

        Thread thread1 = new Thread(() -> {
            synchronized (resource1) {
                System.out.println("Thread one locked: " + resource1.getName());

                try { Thread.sleep(1000); } catch (InterruptedException e) {}

                synchronized (resource2) {
                    System.out.println("Thread one locked - inner sync: " + resource2.getName());
                }
            }

        });

        Thread thread2 = new Thread(() -> {
            synchronized (resource2) {
                System.out.println("Thread two locked: " + resource2.getName());

                try { Thread.sleep(1000); } catch (InterruptedException e) {}

                synchronized (resource1) {
                    System.out.println("Thread two locked - inner sync: " + resource1.getName());
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}
