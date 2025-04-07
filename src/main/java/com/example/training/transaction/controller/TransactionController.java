package com.example.training.transaction.controller;

import com.example.training.transaction.service.OuterTransactionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final OuterTransactionService outerTransactionService;

    public TransactionController(OuterTransactionService outerTransactionService) {
        this.outerTransactionService = outerTransactionService;
    }

    @PostMapping
    public void create() {
        this.outerTransactionService.saveOuterTransaction(false, false);
    }
}
