package com.unibet.worktest.bank;

import com.unibet.worktest.bank.repository.AccountRepository;
import com.unibet.worktest.bank.repository.TransactionRepository;
import com.unibet.worktest.bank.util.BeanUtil;

public class BankFactoryImpl implements BankFactory {

    @Override
    public AccountService getAccountService() {
        return BeanUtil.getBean(AccountService.class);
    }

    @Override
    public TransferService getTransferService() {
        return BeanUtil.getBean(TransferService.class);
    }

    @Override
    public void setupInitialData() {
        AccountRepository accountRepository = BeanUtil.getBean(AccountRepository.class);
        accountRepository.alterToSetReferentialIntegrityFalse();
        accountRepository.truncate();
        accountRepository.alterToSetReferentialIntegrityTrue();

        TransactionRepository transactionRepository = BeanUtil.getBean(TransactionRepository.class);
        transactionRepository.alterToSetReferentialIntegrityFalse();
        transactionRepository.truncate();
        transactionRepository.alterToSetReferentialIntegrityTrue();
    }
}
