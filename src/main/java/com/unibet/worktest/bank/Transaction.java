package com.unibet.worktest.bank;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Value object representing a monetary transaction between two or more accounts. Each
 * account transaction is represented by a transaction leg.
 *
 * @see TransactionLeg
 * @author Unibet
 */
@Sealed
public final class Transaction implements Serializable {
    private final String reference;

    private final String type;

    private final Date date;

    private final List<TransactionLeg> legs;

    public Transaction(String reference, String type,
                       Date date, List<TransactionLeg> legs) {
        if (reference == null) {
            throw new NullPointerException("reference is null");
        }
        if (date == null) {
            throw new NullPointerException("date is null");
        }
        if (legs == null) {
            throw new NullPointerException("legs is null");
        }
        this.reference = reference;
        this.type = type;
        this.date = date;
        this.legs = legs;
    }

    public String getReference() {
        return reference;
    }

    public String getType() {
        return type;
    }

    public List<TransactionLeg> getLegs() {
        return Collections.unmodifiableList(legs);
    }

    public Date getDate() {
        return new Date(date.getTime());
    }
}
