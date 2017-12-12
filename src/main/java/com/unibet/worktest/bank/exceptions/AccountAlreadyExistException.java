package com.unibet.worktest.bank.exceptions;

import com.unibet.worktest.bank.BusinessException;

/**
 * Exception class to validate if Account already exist in data store
 */
public class AccountAlreadyExistException extends BusinessException {

    /**
     * Constructor to throw AccountAlreadyExistException
     * @param accountRef String
     */
    public AccountAlreadyExistException(String accountRef) {
        super("Account already exists with reference '" + accountRef + "'");
    }
}
