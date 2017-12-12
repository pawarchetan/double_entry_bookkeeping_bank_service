package com.unibet.worktest.bank.services.impl;

import com.unibet.worktest.bank.Transaction;
import com.unibet.worktest.bank.TransactionLeg;
import com.unibet.worktest.bank.TransferRequest;
import com.unibet.worktest.bank.TransferService;
import com.unibet.worktest.bank.repository.AccountRepository;
import com.unibet.worktest.bank.repository.TransactionRepository;
import com.unibet.worktest.bank.model.Account;
import com.unibet.worktest.bank.model.AccountTransaction;
import com.unibet.worktest.bank.model.AccountTransactionLeg;
import com.unibet.worktest.bank.validator.AccountValidator;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.unibet.worktest.bank.validator.TransactionValidator.validateTransactionLegsAreBalanced;
import static com.unibet.worktest.bank.validator.TransactionValidator.validateRequest;

/**
 * Implementation class for {@link Transaction} service.
 */
@Service("transferService")
@Transactional(readOnly = true)
public class TransferServiceImpl implements TransferService {

    private TransactionRepository transactionRepository;

    private AccountRepository accountRepository;

    @Autowired
    public TransferServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Executes a balanced, multi-legged monetary transaction as a single unit of work.
     *
     * @param transferRequest a transfer request describing the transactions legs
     * @throws IllegalArgumentException If the entries are less than two or other key properties are missing
     * @throws com.unibet.worktest.bank.AccountNotFoundException if a specified account does not exist
     * @throws com.unibet.worktest.bank.InsufficientFundsException if a participating account is overdrawn
     * @throws com.unibet.worktest.bank.UnbalancedLegsException if the transaction legs are unbalanced
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void transferFunds(TransferRequest transferRequest) {

        validateRequest(transferRequest);
        validateTransactionLegsAreBalanced(transferRequest);

        AccountTransaction transaction = new AccountTransaction(transferRequest);
        Map<String, BigDecimal> accountToNetTransactionMap = getNetTransactionByAccountRef(
                transferRequest.getTransactionLegs());

        List<AccountTransactionLeg> transactionLegsToSave = getTransactionLegs(accountToNetTransactionMap, transaction, transferRequest);
        transaction.setTransactionLegs(transactionLegsToSave);
        transactionRepository.save(transaction);
    }

    /**
     * Finds all monetary transactions performed towards a given account.
     *
     * @param accountRef the client defined account reference to find transactions for
     * @return list of transactions or an empty list if none is found
     * @throws com.unibet.worktest.bank.AccountNotFoundException if the specified account does not exist
     */
    @Override
    public List<Transaction> findTransactions(String accountRef) {
        Validate.notNull(accountRef, "Please provide a valid account reference.");
        List<AccountTransaction> transactions = transactionRepository.findByAccountRef(accountRef);
        return mapToTransactionValueObjectList(transactions);
    }

    /**
     * Get a given transaction by reference.
     *
     * @param transactionRef the transaction reference
     * @return the transaction or null if it doesnt exist
     */
    @Override
    public Transaction getTransaction(String transactionRef) {
        Validate.notNull(transactionRef, "Please provide a valid transaction reference.");
        AccountTransaction transaction = transactionRepository.findByTransactionRef(transactionRef);
        return transaction.toTransactionValueObject();
    }

    private List<Transaction> mapToTransactionValueObjectList(List<AccountTransaction> transactions) {
        List<Transaction> transactionDTOList = new ArrayList<>();
        transactions.forEach(accountTransaction -> transactionDTOList.add(accountTransaction.toTransactionValueObject()));
        return transactionDTOList;
    }

    private Map<String, BigDecimal> getNetTransactionByAccountRef(List<TransactionLeg> transactionLegList) {

        Map<String, BigDecimal> accountToNetTransactionMap = new HashMap<>();
        transactionLegList.forEach(transactionLeg -> {
            String accountReference = transactionLeg.getAccountRef();
            Validate.notNull(accountReference, "Please provide account reference for Transaction Leg");
            if (accountToNetTransactionMap.containsKey(accountReference)) {
                accountToNetTransactionMap.put(accountReference,
                        accountToNetTransactionMap.get(accountReference).add(transactionLeg.getAmount().getAmount()));
            } else {
                accountToNetTransactionMap.put(accountReference, transactionLeg.getAmount().getAmount());
            }
        });
        return accountToNetTransactionMap;
    }

    private List<AccountTransactionLeg> filterTransactionLegsByAccount(Account account, AccountTransaction transactionEntity,
                                                                       List<TransactionLeg> transactionLegList) {
        List<AccountTransactionLeg> legsEntityList = new ArrayList<>();
        transactionLegList.forEach(transactionLeg -> {
            if (account.getAccountRef().equals(transactionLeg.getAccountRef())) {
                legsEntityList.add(new AccountTransactionLeg(account, transactionLeg, transactionEntity));
            }
        });
        return legsEntityList;
    }

    private List<AccountTransactionLeg> getTransactionLegs(Map<String, BigDecimal> accountToNetTransactionMap, AccountTransaction transactionEntity,
                                                           TransferRequest transferRequest){
        List<AccountTransactionLeg> transactionLegsToSave = new ArrayList<>();
        accountToNetTransactionMap.forEach((String accRef, BigDecimal amount) -> {
            Account account = accountRepository.findByAccountRef(accRef);
            AccountValidator.validateIfAccountExist(account, accRef);
            account.performTransaction(amount);
            accountRepository.save(account);
            List<AccountTransactionLeg> transactionLegEntityList = filterTransactionLegsByAccount(account,
                    transactionEntity, transferRequest.getTransactionLegs());
            transactionLegsToSave.addAll(transactionLegEntityList);
        });
        return transactionLegsToSave;
    }
}
