package com.example.training.transaction.service;

import com.example.training.transaction.entity.Transaction;
import com.example.training.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OuterTransactionService {

    private final TransactionRepository repository;
    private final InnerTransactionService innerTransactionService;

    public OuterTransactionService(InnerTransactionService innerTransactionService, TransactionRepository repository) {
        this.innerTransactionService = innerTransactionService;
        this.repository = repository;
    }


    @Transactional
    public void saveOuterTransaction(boolean failOuter, boolean failInner) {
        repository.save(new Transaction("Outer Start"));
        System.out.println("Outer transaction saved: Outer Start");

        try {
            if (failInner) {
                innerTransactionService.saveWithRequiresNew(null); // This will fail due to null constraint
            } else {
                innerTransactionService.saveWithRequiresNew("Inner Success");
            }
        } catch (Exception e) {
            System.out.println("Inner transaction failed, but outer continues...");
        }

        if (failOuter) {
            throw new RuntimeException("Outer transaction failed!");
        }

        repository.save(new Transaction("Outer End"));
        System.out.println("Outer transaction saved: Outer End");
    }


}
