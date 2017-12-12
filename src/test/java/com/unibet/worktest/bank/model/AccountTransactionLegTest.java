package com.unibet.worktest.bank.model;

import com.unibet.worktest.bank.TransactionLeg;
import com.unibet.worktest.bank.BankTestUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class AccountTransactionLegTest {

    private Account account;

    private AccountTransaction transaction;

    private AccountTransactionLeg transactionLeg;

    @Before
    public void Setup() {
        transactionLeg = new AccountTransactionLeg();
        account = Mockito.mock(Account.class);
        transaction = Mockito.mock(AccountTransaction.class);
        Mockito.when(account.getAccountRef()).thenReturn("TRANSFER:1:EUR");
        Mockito.when(transaction.getTransactionRef()).thenReturn("TX100");
    }

    @Test
    public void getterSetterShouldWork() {
        transactionLeg.setAccount(account);
        transactionLeg.setTransaction(transaction);
        transactionLeg.setAmount(BankTestUtil.MONEY1200EUR.getAmount());
        transactionLeg.setCurrency(BankTestUtil.MONEY1200EUR.getCurrency().getCurrencyCode());

        // then
        assertEquals(account.getAccountRef(), transactionLeg.getAccount().getAccountRef());
        assertEquals(transaction.getTransactionRef(), transactionLeg.getTransaction().getTransactionRef());
        assertEquals(BankTestUtil.MONEY1200EUR.getAmount(), transactionLeg.getAmount());
        assertEquals(BankTestUtil.MONEY1200EUR.getCurrency().getCurrencyCode(), transactionLeg.getCurrency());
    }

    @Test
    public void shouldConvertToValueObject() {
        transactionLeg.setAccount(account);
        transactionLeg.setTransaction(transaction);
        transactionLeg.setAmount(BankTestUtil.MONEY1200EUR.getAmount());
        transactionLeg.setCurrency(BankTestUtil.MONEY1200EUR.getCurrency().getCurrencyCode());
        // when
        TransactionLeg transactionLegVO = transactionLeg.toTransactionLegValueObject();
        // then
        assertEquals(account.getAccountRef(), transactionLegVO.getAccountRef());
        assertEquals(BankTestUtil.MONEY1200EUR, transactionLegVO.getAmount());

    }
}
