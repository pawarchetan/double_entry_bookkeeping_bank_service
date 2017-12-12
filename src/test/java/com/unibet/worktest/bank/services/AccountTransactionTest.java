package com.unibet.worktest.bank.services;

import com.unibet.worktest.bank.AccountNotFoundException;
import com.unibet.worktest.bank.AccountService;
import com.unibet.worktest.bank.TransferRequest;
import com.unibet.worktest.bank.TransferService;
import com.unibet.worktest.bank.UnbalancedLegsException;
import com.unibet.worktest.bank.BankTestUtil;
import com.unibet.worktest.bank.launcher.BankApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = BankApplication.class)
@DataJpaTest
@RunWith(SpringJUnit4ClassRunner.class)
public class AccountTransactionTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransferService transferService;

    @Before
    public void setup() {
        accountService.createAccount("TRANSFER:1:EUR", BankTestUtil.MONEY1200EUR);
        accountService.createAccount("TRANSFER:2:EUR", BankTestUtil.MONEY1200EUR);
    }

    // testing the scenario when net transaction is not balanced
    @Test(expected = UnbalancedLegsException.class)
    public void shouldFailIfTransactionRequestIsNotBalanced() {
        transferService.transferFunds(TransferRequest.builder().reference("tx1").account("TRANSFER:1:EUR")
                .amount(BankTestUtil.MONEY_ADD_100EUR).account("TRANSFER:2:EUR").amount(BankTestUtil.MONEY_ADD_100EUR)
                .build());
    }

    @Test(expected = AccountNotFoundException.class)
    public void shouldFailIfAnyAccountDoesnotExist() {
        transferService.transferFunds(TransferRequest.builder().reference("tx1").account("TRANSFER:4:EUR")
                .amount(BankTestUtil.MONEY_ADD_100EUR).account("TRANSFER:2:EUR").amount(BankTestUtil.MONEY_DEDUCT_100EUR)
                .build());
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailIfTransaferRequestIsNull() {
        transferService.transferFunds(null);
    }

}
