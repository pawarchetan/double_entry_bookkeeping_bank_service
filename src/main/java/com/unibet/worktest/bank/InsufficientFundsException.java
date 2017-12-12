package com.unibet.worktest.bank;

/**
 * Business exception thrown if an account participating in a transaction
 * has insufficient funds.
 *
 * @author Unibet
 */
@Sealed
public class InsufficientFundsException extends BusinessException {
    public InsufficientFundsException(String accountRef) {
        super("Insufficient funds for '" + accountRef + "'");
    }
}
