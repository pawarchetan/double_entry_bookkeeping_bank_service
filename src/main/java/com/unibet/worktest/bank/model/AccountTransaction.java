package com.unibet.worktest.bank.model;

import com.unibet.worktest.bank.Transaction;
import com.unibet.worktest.bank.TransactionLeg;
import com.unibet.worktest.bank.TransferRequest;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Model class to manage {@link Transaction} instances.
 */
@Entity
@Table(name = "ACCOUNT_TRANSACTION", uniqueConstraints = @UniqueConstraint(columnNames = { "TRANSACTION_REF" }))
public class AccountTransaction {

    @Id
    @Column(name = "TRANSACTION_ID")
    @GeneratedValue(generator = "SEQ_TRANSACTION_ID", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "SEQ_TRANSACTION", allocationSize = 1, name = "SEQ_TRANSACTION_ID")
    private Long transactionId;

    @Column(name = "TRANSACTION_REF", nullable = false)
    private String transactionRef;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "DATE", columnDefinition = "DATETIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "transaction")
    private List<AccountTransactionLeg> transactionLegs;

    public AccountTransaction() {

    }

    public AccountTransaction(TransferRequest transferRequest) {
        this.date = new Date();
        this.transactionRef = transferRequest.getReference();
        this.type = transferRequest.getType();
        this.transactionLegs = new ArrayList<>();
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<AccountTransactionLeg> getTransactionLegs() {
        return transactionLegs;
    }

    public void setTransactionLegs(List<AccountTransactionLeg> transactionLegs) {
        this.transactionLegs = transactionLegs;
    }

    /**
     * method to convert the entity to a {@link Transaction}. The
     * corresponding {@link TransactionLeg} are linked as an {@link ArrayList}
     * to the transaction.
     *
     * @return the instance of {@link Transaction}
     */
    public Transaction toTransactionValueObject() {
        List<TransactionLeg> transactionLegDTOList = new ArrayList<>();
        this.transactionLegs.forEach(transactionLeg -> transactionLegDTOList.add(transactionLeg.toTransactionLegValueObject()));
        return new Transaction(this.transactionRef, this.type, this.date, transactionLegDTOList);
    }
}
