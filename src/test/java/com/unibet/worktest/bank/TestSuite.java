package com.unibet.worktest.bank;

import com.unibet.worktest.bank.model.AccountTest;
import com.unibet.worktest.bank.model.AccountTransactionLegTest;
import com.unibet.worktest.bank.model.AccountTransactionTest;
import com.unibet.worktest.bank.repository.AccountRepositoryTest;
import com.unibet.worktest.bank.repository.AccountTransactionRepositoryTest;
import com.unibet.worktest.bank.services.AccountServiceTest;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccountTest.class,
        AccountTransactionLegTest.class,
        AccountTransactionTest.class,
        AccountRepositoryTest.class,
        AccountTransactionRepositoryTest.class,
        AccountServiceTest.class,
        com.unibet.worktest.bank.services.AccountTransactionTest.class,
        BankUnitTest.class,
        BankFunctionalTest.class
        })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSuite {



}
