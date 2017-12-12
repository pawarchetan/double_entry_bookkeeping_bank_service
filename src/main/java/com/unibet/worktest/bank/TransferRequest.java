package com.unibet.worktest.bank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Request object describing a balanced, multi-legged monetary transaction between two
 * or more accounts. A request must have at least two legs. Different money currencies
 * are allowed as long as the total balance for the legs with the same currency is zero.
 * <p/>
 * This class uses a nested builder state machine to provide a clear building path with
 * compile-time safety and coding guidance.
 *
 * @author Unibet
 */
@Sealed
public final class TransferRequest implements Serializable {
    /**
     * Returns a new builder for creating a transfer request.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new BuilderImpl();
    }

    public interface Builder {
        /**
         * @param transactionRef a client defined transaction reference
         * @return the next build step
         */
        AccountBuilder reference(String transactionRef);
    }

    public interface AccountBuilder {
        /**
         * @param transactionType a arbitrary transaction type
         * @return the next build step
         */
        AccountBuilder type(String transactionType);

        /**
         * @param accountRef the client defined account reference
         * @return the next build step
         */
        AmountBuilder account(String accountRef);

        /**
         * Builds the transfer request based on supplied parameters.
         *
         * @return the transfer request
         */
        TransferRequest build();
    }

    public interface AmountBuilder {
        /**
         * @param money the transfer amount for the given account
         * @return the next build step
         */
        AccountBuilder amount(Money money);
    }

    private static final class BuilderImpl implements Builder, AccountBuilder, AmountBuilder {
        private final TransferRequest request = new TransferRequest();

        private String accountRef;

        @Override
        public AccountBuilder reference(String transactionRef) {
            request.reference = transactionRef;
            return this;
        }

        @Override
        public AccountBuilder type(String transactionType) {
            request.type = transactionType;
            return this;
        }

        @Override
        public AmountBuilder account(String accountRef) {
            this.accountRef = accountRef;
            return this;
        }

        @Override
        public AccountBuilder amount(Money money) {
            request.transactionLegs.add(new TransactionLeg(accountRef, money));
            accountRef = null;
            return this;
        }

        @Override
        public TransferRequest build() {
            if (request.transactionLegs.size() < 2) {
                throw new IllegalStateException("Expected at least two account legs");
            }
            return request;
        }
    }

    private String reference;

    private String type;

    private final List<TransactionLeg> transactionLegs = new ArrayList<>();

    private TransferRequest() {
    }

    public List<TransactionLeg> getTransactionLegs() {
        return Collections.unmodifiableList(transactionLegs);
    }

    public String getReference() {
        return reference;
    }

    public String getType() {
        return type;
    }
}
