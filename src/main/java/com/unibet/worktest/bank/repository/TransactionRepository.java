package com.unibet.worktest.bank.repository;

import com.unibet.worktest.bank.model.Account;
import com.unibet.worktest.bank.model.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository to manage {@link AccountTransaction} instances.
 */
@Repository
@Transactional(readOnly = true)
public interface TransactionRepository extends JpaRepository<AccountTransaction, Long> {

    /**
     * Saves the given {@link AccountTransaction}
     * @param transaction AccountTransaction
     * @param <S> S
     * @return {@link AccountTransaction}
     */
    @Modifying
    @Transactional
    <S extends AccountTransaction> S save(S transaction);

    /**
     * method to return {@link AccountTransaction} by accountRef
     * @param accountRef String
     * @return
     */
    @Query("select distinct trans from AccountTransaction trans INNER JOIN FETCH trans.transactionLegs legs INNER JOIN FETCH legs.account acc where acc.accountRef = :accountRef")
    List<AccountTransaction> findByAccountRef(@Param("accountRef") String accountRef);

    /**
     * method to return {@link AccountTransaction} by transactionRef
     * @param transactionRef String
     * @return {@link AccountTransaction}
     */
    AccountTransaction findByTransactionRef(String transactionRef);

    /**
     * method to truncate table {@link AccountTransaction} - set REFERENTIAL_INTEGRITY to false
     */
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE Account_Transaction", nativeQuery = true)
    void truncate();

    /**
     * method to alter {@link AccountTransaction} - set REFERENTIAL_INTEGRITY to false
     */
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE Account_Transaction SET REFERENTIAL_INTEGRITY FALSE", nativeQuery = true)
    void alterToSetReferentialIntegrityFalse();

    /**
     * method to alter {@link AccountTransaction} - set REFERENTIAL_INTEGRITY to true
     */
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE Account_Transaction SET REFERENTIAL_INTEGRITY TRUE", nativeQuery = true)
    void alterToSetReferentialIntegrityTrue();
}
