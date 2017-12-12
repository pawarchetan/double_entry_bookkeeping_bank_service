package com.unibet.worktest.bank.model;

import com.unibet.worktest.bank.InsufficientFundsException;
import com.unibet.worktest.bank.Money;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

/**
 * Model class to manage {@link Account} instances.
 */
@Entity
@Table(name = "ACCOUNT", uniqueConstraints = @UniqueConstraint(columnNames = { "ACCOUNT_REF" }))
public class Account {

    @Id
    @GeneratedValue(generator = "SEQ_ACCOUNT_ID", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "SEQUENCE_ACCOUNT", allocationSize = 1, name = "SEQ_ACCOUNT_ID")
    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @Column(name = "ACCOUNT_REF", nullable = false)
    private String accountRef;

    @Column(name = "ACCOUNT_BALANCE", nullable = false)
    private BigDecimal accountBalance;

    @Column(name = "CURRENCY", nullable = false)
    private String currency;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
    private List<AccountTransactionLeg> transactionLegs;

    public Account() {

    }

    public Account(String accountRef, Money money) {
        this.accountRef = accountRef;
        this.accountBalance = money.getAmount();
        this.currency = money.getCurrency().getCurrencyCode();
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountRef() {
        return accountRef;
    }

    public void setAccountRef(String accountRef) {
        this.accountRef = accountRef;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<AccountTransactionLeg> getTransactionLegs() {
        return transactionLegs;
    }

    public void setTransactionLegs(List<AccountTransactionLeg> transactionLegs) {
        this.transactionLegs = transactionLegs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return accountId.equals(account.accountId) && accountRef.equals(account.accountRef);
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        hashCode = 37 * hashCode + (int) (this.accountId ^ (this.accountId >>> 32));
        hashCode = 37 * hashCode + this.accountRef.hashCode();
        return hashCode;
    }

    @Override
    public String toString() {
        return "Account" +
                "[ id = " + this.accountId +
                ", accountRef = " + this.accountRef +
                ", balance = " + this.accountBalance + " " + this.currency +
                "]";
    }

    /**
     * This method performs a transaction on an Account. It first checks if the
     * transaction will lead to negative balance. Successful transactions are carried out.
     *
     * @param transactionAmount transaction amount to be performed on the account
     * @throws InsufficientFundsException if account balance would be negative performing the transaction
     */
    public void performTransaction(BigDecimal transactionAmount) {
        BigDecimal balance = this.accountBalance.add(transactionAmount);
        if (balance.signum() < 0) {
            throw new InsufficientFundsException(this.accountRef);
        }
        this.accountBalance = balance;
    }

    /**
     * Method to get the account balance.
     *
     * @return an instance of {@link Money}
     */
    public Money getBalance() {
        Currency currency = Currency.getInstance(this.currency);
        return new Money(this.accountBalance, currency);
    }
}
