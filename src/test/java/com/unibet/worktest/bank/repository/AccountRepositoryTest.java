package com.unibet.worktest.bank.repository;

import com.unibet.worktest.bank.launcher.BankApplication;
import com.unibet.worktest.bank.model.Account;
import com.unibet.worktest.bank.BankTestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@SpringBootTest(classes = BankApplication.class)
@DataJpaTest
@RunWith(SpringJUnit4ClassRunner.class)
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Before
    public void setup() {
        accountRepository.save(new Account("CASH:1:EUR", BankTestUtil.MONEY1200EUR));
        accountRepository.flush();
    }

    @Test
    public void shouldSaveAccountSuccessfully() {
        // when
        Account account = new Account("CASH:2:USD", BankTestUtil.MONEY1000USD);
        account = accountRepository.save(account);

        // then
        assertNotNull(account.getAccountId());
        assertEquals(2, accountRepository.findAll().size());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldNotSaveAccountWithExistingAccountRef() {

        Account account = new Account("CASH:1:EUR", BankTestUtil.MONEY1200EUR);
        accountRepository.save(account);
        accountRepository.flush();
    }

    @Test
    public void shouldReturnAccountWithExistingAccountRef() {

        Account account = accountRepository.findByAccountRef("CASH:1:EUR");
        assertNotNull(account);
    }

    @Test
    public void shouldReturnNullWithInvalidAccountRef() {

        Account account = accountRepository.findByAccountRef("CASH:4:EUR");
        assertNull(account);
    }

}
