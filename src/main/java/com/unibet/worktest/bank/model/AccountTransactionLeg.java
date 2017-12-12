package com.unibet.worktest.bank.model;

import com.unibet.worktest.bank.Money;
import com.unibet.worktest.bank.Transaction;
import com.unibet.worktest.bank.TransactionLeg;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Model class to manage {@link TransactionLeg} instances.
 */
@Entity
@Table(name = "TRANSACTION_LEG")
public class AccountTransactionLeg {

    @Id
    @GeneratedValue(generator = "SEQ_TRANSACTION_LEG_ID", strategy = GenerationType.SEQUENCE)
    @Column(name = "TRANS_LEG_ID")
    @SequenceGenerator(sequenceName = "SEQ_TRANSACTION_LEG", allocationSize = 1, name = "SEQ_TRANSACTION_LEG_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TRANSACTION_ID")
    private AccountTransaction transaction;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "ACCOUNT_REF")
    private Account account;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "CURRENCY", nullable = false)
    private String currency;

    public AccountTransactionLeg(){

    }

    public AccountTransactionLeg(Account account, TransactionLeg transactionLeg, AccountTransaction transaction) {
        this.account = account;
        this.amount = transactionLeg.getAmount().getAmount();
        this.currency = transactionLeg.getAmount().getCurrency().getCurrencyCode();
        this.transaction = transaction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(AccountTransaction transaction) {
        this.transaction = transaction;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Convenience method to convert the entity to a TransactionLeg
     *
     * @return a {@link TransactionLeg}
     */
    TransactionLeg toTransactionLegValueObject() {
        return new TransactionLeg(this.account.getAccountRef(), Money.create(this.amount.toString(), this.currency));
    }
}
