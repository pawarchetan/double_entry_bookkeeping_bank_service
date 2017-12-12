package com.unibet.worktest.bank.validator;

import com.unibet.worktest.bank.AccountNotFoundException;
import com.unibet.worktest.bank.Money;
import com.unibet.worktest.bank.exceptions.AccountAlreadyExistException;
import com.unibet.worktest.bank.model.Account;
import org.apache.commons.lang3.Validate;

import java.util.logging.Logger;

/**
 * Utility class to validate {@link Account}.
 */
public class AccountValidator {

    private static final Logger logger = Logger.getLogger(AccountValidator.class.getName());

    /**
     * @throws java.lang.IllegalArgumentException if any of the required parameters are missing
     * @param accountRef String
     * @param amount Money
     */
    public static void validateAccount(String accountRef, Money amount){
        validateAccountRef(accountRef);
        validateAmount(amount);
        validateCurrency(amount);
        validateNegativeAmount(amount);
    }


    /**
     *
     * @throws AccountAlreadyExistException if account already exist in database
     * @param account Account
     * @param accountRef String
     */
    public static void validateIfAccountAlreadyExist(Account account, String accountRef){
        if (AccountValidator.validateIfAccountAlreadyExist(account)) {
            logger.severe("Account already exists with ref " + accountRef);
            throw new AccountAlreadyExistException(accountRef);
        }
    }

    /**
     * @throws AccountNotFoundException if account is not exist in database
     * @param account Account
     * @param accountRef String
     */
    public static void validateIfAccountExist(Account account, String accountRef){
        if (account == null) {
            logger.severe("Cannot find an Account with reference " + accountRef);
            throw new AccountNotFoundException(accountRef);
        }
    }

    private static boolean validateIfAccountAlreadyExist(Account account){
        return account != null;
    }

    private static void validateAccountRef(String accountRef){
        Validate.notNull(accountRef, "Please provide a valid account reference.");
    }

    private static void validateAmount(Money amount){
        Validate.notNull(amount, "Please provide a valid amount.");
    }

    private static void validateCurrency(Money amount){
        Validate.notNull(amount.getCurrency(), "Please provide a valid currency.");
    }

    private static void validateNegativeAmount(Money amount){
        Validate.isTrue(amount.getAmount().signum() >= 0, "Cannot create account with negative balance.");
    }
}
