package com.unibet.worktest.bank.validator;

import com.unibet.worktest.bank.TransactionLeg;
import com.unibet.worktest.bank.TransferRequest;
import com.unibet.worktest.bank.UnbalancedLegsException;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class to validate {@link com.unibet.worktest.bank.Transaction}.
 */
public class TransactionValidator {

    /**
     * @throws UnbalancedLegsException if the legs are not balanced
     * @param transferRequest TransferRequest
     */
    public static void validateTransactionLegsAreBalanced(TransferRequest transferRequest) {
        List<TransactionLeg> legs = transferRequest.getTransactionLegs();
        Map<Currency, BigDecimal> currencyMap = getTotalByCurrency(legs);
        for (BigDecimal total : currencyMap.values()) {
            if (total.compareTo(BigDecimal.ZERO) != 0) {
                throw new UnbalancedLegsException("amount should be 0 but was" + total);
            }
        }
    }

    /**
     * @throws  java.lang.IllegalArgumentException if any of the required parameters are missing
     * @param transferRequest TransferRequest
     */
    public static void validateRequest(TransferRequest transferRequest) {
        validateTransferRequestReference(transferRequest);
        validateTransferRequestTransactionLegs(transferRequest);
        validateNumbersOfLegsInTransferRequest(transferRequest);
    }

    private static void validateTransferRequestReference(TransferRequest transferRequest){
        if (StringUtils.isEmpty(transferRequest.getReference()))
            throw new IllegalArgumentException("Transaction reference required");
    }

    private static void validateTransferRequestTransactionLegs(TransferRequest transferRequest){
        if (transferRequest.getTransactionLegs().isEmpty())
            throw new IllegalArgumentException("No Transaction Legs defined");
    }

    private static void validateNumbersOfLegsInTransferRequest(TransferRequest transferRequest){
        if (transferRequest.getTransactionLegs().size() < 2)
            throw new IllegalArgumentException("Transaction leg entries should be atleast two");
    }

    private static Map<Currency, BigDecimal> getTotalByCurrency(List<TransactionLeg> legs) {
        Map<Currency, BigDecimal> currencyMap = new HashMap<>();
        legs.forEach(leg -> {
            BigDecimal amount = currencyMap.get(leg.getAmount().getCurrency());
            if (amount == null) {
                amount = BigDecimal.ZERO;
            }
            currencyMap.put(leg.getAmount().getCurrency(), amount.add(leg.getAmount().getAmount()));
        });
        return currencyMap;
    }
}
