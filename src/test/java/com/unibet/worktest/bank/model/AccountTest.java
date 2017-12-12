package com.unibet.worktest.bank.model;

import com.unibet.worktest.bank.InsufficientFundsException;
import com.unibet.worktest.bank.BankTestUtil;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AccountTest {

    private Account account;
    private List<AccountTransactionLeg> transactionLegList;

    @Before
    public void setup() {
        account = new Account();
        transactionLegList = getTransactionLegList();
    }

    @Test
    public void getterSetterShouldWork() {
        account.setAccountId(12345L);
        account.setAccountRef("DEPOSIT:1:USD");
        account.setAccountBalance(BankTestUtil.MONEY1000USD.getAmount());
        account.setCurrency(BankTestUtil.MONEY1000USD.getCurrency().getCurrencyCode());
        account.setTransactionLegs(transactionLegList);

        // then
        assertEquals(Long.valueOf(12345), account.getAccountId());
        assertEquals("DEPOSIT:1:USD", account.getAccountRef());
        assertEquals(BankTestUtil.MONEY1000USD.getAmount(), account.getAccountBalance());
        assertEquals(BankTestUtil.MONEY1000USD.getCurrency().getCurrencyCode(), account.getCurrency());
        assertEquals(transactionLegList, account.getTransactionLegs());
    }

    @Test
    public void shouldAddAmountCorrectly() {
        account.setAccountBalance(BankTestUtil.MONEY1000USD.getAmount());
        account.performTransaction(BigDecimal.valueOf(100));

        // then
        assertEquals(BigDecimal.valueOf(1100), account.getAccountBalance());
    }

    @Test
    public void shouldDeductAmountCorrectly() {
        account.setAccountBalance(BankTestUtil.MONEY1000USD.getAmount());
        account.performTransaction(BigDecimal.valueOf(-100));

        // then
        assertEquals(BigDecimal.valueOf(900), account.getAccountBalance());
    }

    @Test(expected = InsufficientFundsException.class)
    public void shouldThrowExceptionWhenOverDrawn() {
        account.setAccountBalance(BankTestUtil.MONEY1000USD.getAmount());
        account.performTransaction(BigDecimal.valueOf(-1001));

    }

    private List<AccountTransactionLeg> getTransactionLegList() {
        List<AccountTransactionLeg> transactionLegList = new ArrayList<>();
        transactionLegList.add(new AccountTransactionLeg());
        return transactionLegList;
    }
}
