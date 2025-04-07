package com.example.training.transaction.service;

import com.example.training.transaction.entity.Transaction;
import com.example.training.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InnerTransactionService {

    private final TransactionRepository repository;

    public InnerTransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveWithRequiresNew(String name) {
        repository.save(new Transaction(name));
        System.out.println("Inner transaction saved: " + name);
    }
}
