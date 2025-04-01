package com.example.training.concurrency;

import java.util.concurrent.*;

public class CallableCalculation {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Callable<Integer> task = () -> {
            int sum = 0;
            for (int i =1; i<= 1000; i++) {
                sum+=i;
            }

            return sum;
        };

        Future<Integer> future =executorService.submit(task);

        var result = future.get();

        System.out.println("Result of calculation: " + result);

        executorService.shutdown();
    }
}
