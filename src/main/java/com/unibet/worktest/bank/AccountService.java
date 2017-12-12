package com.unibet.worktest.bank;

/**
 * Defines the business logic for managing monetary accounts.
 *
 * @author Unibet
 */
@Sealed
public interface AccountService {
    /**
     * Create a new account with an initial balance.
     *
     * @param accountRef a client defined account reference
     * @param amount the initial account balance
     * @throws com.unibet.worktest.bank.BusinessException if there are any problems creating the account
     */
    void createAccount(String accountRef, Money amount);

    /**
     * Get the current balance for a given account.
     *
     * @param accountRef the client definied account reference
     * @return the account balance
     * @throws com.unibet.worktest.bank.BusinessException if there are any problems retrieving the balance
     * or the account is not found
     */
    Money getAccountBalance(String accountRef);
}
