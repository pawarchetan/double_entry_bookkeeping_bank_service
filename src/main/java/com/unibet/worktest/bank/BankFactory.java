package com.unibet.worktest.bank;

/**
 * Factory interface for creating instances of central business services.
 *
 * @author Unibet
 */
@Sealed
public interface BankFactory {
    /**
     * @return an instance of the AccountService providing account management
     */
    AccountService getAccountService();

    /**
     * @return an instance of the TransferService providing account transfers
     */
    TransferService getTransferService();

    /**
     * Support method for setting up the initial state of the persistent
     * store between test runs.
     */
    void setupInitialData();
}
