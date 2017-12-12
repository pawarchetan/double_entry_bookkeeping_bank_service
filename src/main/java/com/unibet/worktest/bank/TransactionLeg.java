package com.unibet.worktest.bank;

import java.io.Serializable;

/**
 * Value object representing a single monetary transaction towards an account.
 *
 * @see Transaction
 * @author Unibet
 */
@Sealed
public final class TransactionLeg implements Serializable {
    private final String accountRef;

    private final Money amount;

    public TransactionLeg(String accountRef, Money amount) {
        if (accountRef == null) {
            throw new NullPointerException("accountRef is null");
        }
        if (amount == null) {
            throw new NullPointerException("amount is null");
        }
        this.accountRef = accountRef;
        this.amount = amount;
    }

    public Money getAmount() {
        return amount;
    }

    public String getAccountRef() {
        return accountRef;
    }
}

