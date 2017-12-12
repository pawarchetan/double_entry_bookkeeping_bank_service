package com.unibet.worktest.bank;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertEquals;

/**
 * Simple unit test that demonstrates the core functionality of the banking system.
 *
 * @author Unibet
 */
@Sealed
public final class BankUnitTest {
    // NOTE: Account names are opaque (no built-in semantics)

    static final String CASH_ACCOUNT = "cash:1:EUR";

    static final String DEPOSIT_ACCOUNT = "deposit:1:EUR";

    private AccountService accountServiceMock;

    private TransferService transferServiceMock;

    @Before
    public void setupAccountServiceMock() {
        accountServiceMock = Mockito.mock(AccountService.class);

        Mockito.when(accountServiceMock.getAccountBalance(CASH_ACCOUNT))
                .thenReturn(Money.create("1000.00", "EUR"))
                .thenReturn(Money.create("984.50", "EUR"));

        Mockito.when(accountServiceMock.getAccountBalance(DEPOSIT_ACCOUNT))
                .thenReturn(Money.create("0.00", "EUR"))
                .thenReturn(Money.create("15.50", "EUR"));
    }

    @Before
    public void setupTransferServiceMock() {
        List<TransactionLeg> tx1 = new ArrayList<>();
        tx1.add(new TransactionLeg(CASH_ACCOUNT, Money.create("-5.00", "EUR")));
        tx1.add(new TransactionLeg(DEPOSIT_ACCOUNT, Money.create("5.00", "EUR")));

        List<TransactionLeg> tx2 = new ArrayList<>();
        tx2.add(new TransactionLeg(DEPOSIT_ACCOUNT, Money.create("10.50", "EUR")));
        tx2.add(new TransactionLeg(CASH_ACCOUNT, Money.create("-10.50", "EUR")));

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("tx1", "testing", new Date(), tx1));
        transactions.add(new Transaction("tx2", "testing", new Date(), tx2));

        transferServiceMock = Mockito.mock(TransferService.class);

        Mockito.doNothing().when(transferServiceMock).transferFunds(Mockito.any(TransferRequest.class));

        Mockito.when(transferServiceMock.findTransactions(CASH_ACCOUNT))
                .thenReturn(transactions);

        Mockito.when(transferServiceMock.findTransactions(DEPOSIT_ACCOUNT))
                .thenReturn(transactions);

        Mockito.when(transferServiceMock.getTransaction("tx1"))
                .thenReturn(transactions.get(0));

        Mockito.when(transferServiceMock.getTransaction("tx2"))
                .thenReturn(transactions.get(1));

        Mockito.when(transferServiceMock.getTransaction("tx3"))
                .thenReturn(null);
    }

    @Test
    public void accountBalancesUpdatedAfterTransfer() {
        assertEquals(Money.create("1000.00", "EUR"),
                accountServiceMock.getAccountBalance(CASH_ACCOUNT));
        assertEquals(Money.create("0.00", "EUR"),
                accountServiceMock.getAccountBalance(DEPOSIT_ACCOUNT));

        // Create two distinct transactions with two legs each

        transferServiceMock.transferFunds(TransferRequest.builder()
                .reference("tx1")
                .account(CASH_ACCOUNT).amount(Money.create("-5.00", "EUR"))
                .account(DEPOSIT_ACCOUNT).amount(Money.create("5.00", "EUR"))
                .build());

        transferServiceMock.transferFunds(TransferRequest.builder()
                .reference("tx2")
                .account(DEPOSIT_ACCOUNT).amount(Money.create("10.50", "EUR"))
                .account(CASH_ACCOUNT).amount(Money.create("-10.50", "EUR"))
                .build());

        assertEquals(Money.create("984.50", "EUR"),
                accountServiceMock.getAccountBalance(CASH_ACCOUNT));
        assertEquals(Money.create("15.50", "EUR"),
                accountServiceMock.getAccountBalance(DEPOSIT_ACCOUNT));

        List<Transaction> tx1 = transferServiceMock.findTransactions(CASH_ACCOUNT);
        assertEquals(2, tx1.size());

        List<Transaction> tx2 = transferServiceMock.findTransactions(DEPOSIT_ACCOUNT);
        assertEquals(2, tx2.size());

        Assert.assertNotNull(transferServiceMock.getTransaction("tx1"));
        Assert.assertNotNull(transferServiceMock.getTransaction("tx2"));
        Assert.assertNull(transferServiceMock.getTransaction("tx3"));

    }
}
