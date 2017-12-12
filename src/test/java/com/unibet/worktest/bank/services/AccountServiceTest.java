package com.unibet.worktest.bank.services;

import com.unibet.worktest.bank.AccountNotFoundException;
import com.unibet.worktest.bank.AccountService;
import com.unibet.worktest.bank.exceptions.AccountAlreadyExistException;
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
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Before
    public void setup() {
        accountService.createAccount("TRANSFER:1:EUR", BankTestUtil.MONEY1200EUR);
    }

    @Test(expected = AccountAlreadyExistException.class)
    public void shouldFailToCreateAccountIfAlreadyExists() {
        accountService.createAccount("TRANSFER:1:EUR", BankTestUtil.MONEY1000USD);
    }

    @Test(expected = AccountNotFoundException.class)
    public void shouldFailToFetchBalanceIfAccountDoesNotExist() {
        accountService.getAccountBalance("TRANSFER:2:EUR");
    }
}
