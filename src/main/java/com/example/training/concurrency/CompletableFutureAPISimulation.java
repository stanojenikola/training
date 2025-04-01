package com.example.training.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.lang.Thread.sleep;

public class CompletableFutureAPISimulation {

    static CompletableFuture<Integer> getExternalUserCount() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 22;
        });
    }

    static CompletableFuture<Integer> getInternalUserCount() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 455;
        });
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var external = getExternalUserCount();
        var internal = getInternalUserCount();

        var result = external.thenCombine(internal, (e, i) -> e + i);

        System.out.println(result.get());
    }
}
