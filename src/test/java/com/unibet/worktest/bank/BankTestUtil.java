package com.unibet.worktest.bank;

import com.unibet.worktest.bank.Money;

import java.util.Currency;

public class BankTestUtil {

    public static final Currency EURO = Currency.getInstance("EUR");

    public static final Currency USD = Currency.getInstance("USD");

    public static final Money MONEY1200EUR = Money.create("1200", EURO);

    public static final Money MONEY1000USD = Money.create("1000", USD);

    public static final Money MONEY_ADD_100EUR = Money.create("100", EURO);

    public static final Money MONEY_DEDUCT_100EUR = Money.create("-100", EURO);
}
