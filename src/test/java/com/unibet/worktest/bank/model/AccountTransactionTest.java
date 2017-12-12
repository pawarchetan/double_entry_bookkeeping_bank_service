package com.unibet.worktest.bank.model;

import com.unibet.worktest.bank.Transaction;
import com.unibet.worktest.bank.TransactionLeg;
import com.unibet.worktest.bank.BankTestUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountTransactionTest {

    private AccountTransaction transaction;

    private AccountTransactionLeg transactionLeg1;
    private AccountTransactionLeg transactionLeg2;

    private TransactionLeg transactionlegvo1;
    private TransactionLeg transactionlegvo2;

    private Date transactionDate;

    @Before
    public void Setup() {
        transaction = new AccountTransaction();

        Account account1 = Mockito.mock(Account.class);
        transactionLeg1 = Mockito.mock(AccountTransactionLeg.class);

        Account account2 = Mockito.mock(Account.class);
        transactionLeg2 = Mockito.mock(AccountTransactionLeg.class);

        transactionlegvo1 = new TransactionLeg("TRANSFER:1:EUR", BankTestUtil.MONEY_ADD_100EUR);
        transactionlegvo2 = new TransactionLeg("TRANSFER:2:EUR", BankTestUtil.MONEY_DEDUCT_100EUR);

        Mockito.when(account1.getAccountRef()).thenReturn("TRANSFER:1:EUR");
        Mockito.when(account2.getAccountRef()).thenReturn("TRANSFER:2:EUR");
        Mockito.when(transactionLeg1.getTransaction()).thenReturn(transaction);
        Mockito.when(transactionLeg2.getTransaction()).thenReturn(transaction);
        Mockito.when(transactionLeg1.getAccount()).thenReturn(account1);
        Mockito.when(transactionLeg2.getAccount()).thenReturn(account2);
        Mockito.when(transactionLeg1.toTransactionLegValueObject()).thenReturn(transactionlegvo1);
        Mockito.when(transactionLeg2.toTransactionLegValueObject()).thenReturn(transactionlegvo2);
        transactionDate = Calendar.getInstance().getTime();
    }

    @Test
    public void getterSetterShouldWork() {
        transaction.setTransactionRef("TX1");
        transaction.setDate(transactionDate);
        transaction.setTransactionLegs(Arrays.asList(transactionLeg1, transactionLeg2));

        // then
        assertEquals("TX1", transaction.getTransactionRef());
        assertEquals(transactionDate, transaction.getDate());
        assertEquals(Arrays.asList(transactionLeg1, transactionLeg2), transaction.getTransactionLegs());

    }

    @Test
    public void shouldConvertToValueObjectCorrectly() {
        transaction.setTransactionRef("TX1");
        transaction.setDate(transactionDate);
        transaction.setTransactionLegs(Arrays.asList(transactionLeg1, transactionLeg2));

        // then
        Transaction transactionVO = transaction.toTransactionValueObject();

        // then
        assertEquals("TX1", transactionVO.getReference());
        assertEquals(transactionDate, transactionVO.getDate());
        assertTrue(transactionVO.getLegs().contains(transactionlegvo1));
        assertTrue(transactionVO.getLegs().contains(transactionlegvo2));

    }
}
