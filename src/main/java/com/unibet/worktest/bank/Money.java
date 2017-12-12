package com.unibet.worktest.bank;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * Immutable monetary money class that couples an amount with a currency.
 * The amount value is represented by {@code java.math.BigDecimal} and the currency
 * by a ISO-4701 {@code java.util.Currency}.
 *
 * @author Unibet
 */
@Sealed
public final class Money implements Serializable, Comparable<Money> {
    public static Money euros(String amount) {
        return create(amount, "EUR");
    }

    public static Money kronor(String amount) {
        return create(amount, "SEK");
    }

    public static Money create(String amount, String currency) {
        return create(amount, Currency.getInstance(currency));
    }

    public static Money create(String amount, Currency currency) {
        return new Money(new BigDecimal(amount), currency);
    }

    private BigDecimal amount;

    private Currency currency;

    /**
     * Creates a new Money instance.
     *
     * @param amount the decimal amount (required)
     * @param currency the currency (required)
     */
    public Money(BigDecimal amount, Currency currency) {
        if (amount == null) {
            throw new NullPointerException("value is null");
        }
        if (currency == null) {
            throw new NullPointerException("currency is null");
        }
        this.amount = amount;
        this.currency = currency;
    }

    protected Money() {
    }

    public Currency getCurrency() {
        return currency;
    }

    /**
     * Return the underlying monetary value as BigDecimal.
     *
     * @return the monetary value
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Compares this money object with another instance. The money objects are
     * compared by their underlying long value.
     * <p/>
     * {@inheritDoc}
     */
    public int compareTo(Money o) {
        return getAmount().compareTo(o.getAmount());
    }

    /**
     * Compares two money objects for equality. The money objects are
     * compared by their underlying bigdecimal value and currency ISO code.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Money money = (Money) o;

        if (!amount.equals(money.amount)) {
            return false;
        }
        if (!currency.equals(money.currency)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = amount.hashCode();
        result = 31 * result + currency.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Money");
        sb.append("{amount=").append(amount);
        sb.append(", currency=").append(currency);
        sb.append('}');
        return sb.toString();
    }
}
