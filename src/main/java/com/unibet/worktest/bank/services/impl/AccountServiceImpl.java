package com.unibet.worktest.bank.services.impl;

import com.unibet.worktest.bank.AccountService;
import com.unibet.worktest.bank.Money;
import com.unibet.worktest.bank.repository.AccountRepository;
import com.unibet.worktest.bank.exceptions.AccountAlreadyExistException;
import com.unibet.worktest.bank.model.Account;
import com.unibet.worktest.bank.validator.AccountValidator;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

import static com.unibet.worktest.bank.validator.AccountValidator.validateAccount;
import static com.unibet.worktest.bank.validator.AccountValidator.validateIfAccountAlreadyExist;
import static com.unibet.worktest.bank.validator.AccountValidator.validateIfAccountExist;

/**
 * Implementation class for {@link Account} service.
 */
@Service("accountService")
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = Logger.getLogger(AccountServiceImpl.class.getName());
    private AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Create a new account with an initial balance.
     *
     * @param accountRef a client defined account reference
     * @param amount the initial account balance
     * @throws com.unibet.worktest.bank.exceptions.AccountAlreadyExistException
     */
    @Override
    @Transactional(readOnly = false)
    public void createAccount(String accountRef, Money amount) {

        validateAccount(accountRef, amount);
        Account account = accountRepository.findByAccountRef(accountRef);
        validateIfAccountAlreadyExist(account, accountRef);
        account = new Account(accountRef, amount);
        saveAccount(account, accountRef);

        logger.info("Account created with Id " + account.getAccountId());
    }

    /**
     * Get the current balance for a given account.
     *
     * @param accountRef the client defined account reference
     * @return the account balance
     * @throws com.unibet.worktest.bank.AccountNotFoundException if the referenced account does not exist
     */
    @Override
    public Money getAccountBalance(String accountRef) {

        Validate.notNull(accountRef, "Please provide a valid account reference.");
        Account account = accountRepository.findByAccountRef(accountRef);
        validateIfAccountExist(account, accountRef);

        logger.info("account found with reference " + accountRef + "and details " + account);
        return account.getBalance();
    }

    private void saveAccount(Account account, String accountRef){
        try {
            accountRepository.save(account);
        } catch (DataIntegrityViolationException ex) {
            logger.severe("Account already exists with ref " + accountRef);
            throw new AccountAlreadyExistException(accountRef);
        }
    }
}
