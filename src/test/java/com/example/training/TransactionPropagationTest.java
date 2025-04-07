package com.example.training;

import com.example.training.transaction.entity.Transaction;
import com.example.training.transaction.repository.TransactionRepository;
import com.example.training.transaction.service.OuterTransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED) // Prevents default rollback in tests
public class TransactionPropagationTest {

    @Autowired
    private OuterTransactionService outerTransactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Before
    public void setup() {
        //transactionRepository.deleteAll(); // Clean DB before each test
    }

    private void printDatabaseState() {
        List<Transaction> transactions = transactionRepository.findAll();
        System.out.println("Database state: " + transactions.stream()
                .map(Transaction::getName)
                .collect(Collectors.toList()));
    }

    @Test
    public void testNoFailure() {
        outerTransactionService.saveOuterTransaction(false, false);
        printDatabaseState();

        List<Transaction> transactions = transactionRepository.findAll();
        assertEquals(3, transactions.size());
        assertEquals("Outer Start", transactions.get(0).getName());
        assertEquals("Inner Success", transactions.get(1).getName());
        assertEquals("Outer End", transactions.get(2).getName());
    }

    @Test
    public void testInnerFailure() {
        outerTransactionService.saveOuterTransaction(false, true);
        printDatabaseState();

        List<Transaction> transactions = transactionRepository.findAll();
        assertEquals(2, transactions.size());
        assertEquals("Outer Start", transactions.get(0).getName());
        assertEquals("Outer End", transactions.get(1).getName());
    }

    @Test
    public void testOuterFailure() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            outerTransactionService.saveOuterTransaction(true, false);
        });

        assertEquals("Outer transaction failed!", exception.getMessage());
        printDatabaseState();

        List<Transaction> transactions = transactionRepository.findAll();
        assertEquals(1, transactions.size());
        assertEquals("Inner Success", transactions.get(0).getName());
    }

    @Test
    public void testBothFail() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            outerTransactionService.saveOuterTransaction(true, true);
        });

        assertEquals("Outer transaction failed!", exception.getMessage());
        printDatabaseState();

        List<Transaction> transactions = transactionRepository.findAll();
        assertEquals(0, transactions.size()); // Nothing should persist
    }
}
