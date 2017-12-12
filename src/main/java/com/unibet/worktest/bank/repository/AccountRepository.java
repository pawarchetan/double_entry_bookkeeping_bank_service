package com.unibet.worktest.bank.repository;

import com.unibet.worktest.bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository to manage {@link Account} instances.
 */
@Repository
@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Saves the given {@link Account}
     * @param account Account
     * @param <S> S
     * @return {@link Account}
     */
    @Modifying
    @Transactional(readOnly = false)
    @Override
    <S extends Account> S save(S account);

    /**
     * method to return {@link Account} by accountRef
     * @param accountRef String
     * @return {@link Account}
     */
    Account findByAccountRef(String accountRef);

    /**
     * method truncate {@link Account} table
     */
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE account", nativeQuery = true)
    void truncate();

    /**
     * method to alter {@link Account} table (set REFERENTIAL_INTEGRITY to false)
     */
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE account SET REFERENTIAL_INTEGRITY FALSE", nativeQuery = true)
    void alterToSetReferentialIntegrityFalse();

    /**
     * method to alter {@link Account} table (set REFERENTIAL_INTEGRITY to true)
     */
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE account SET REFERENTIAL_INTEGRITY TRUE", nativeQuery = true)
    void alterToSetReferentialIntegrityTrue();
}
