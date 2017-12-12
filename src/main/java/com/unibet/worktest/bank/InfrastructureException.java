package com.unibet.worktest.bank;

/**
 * Exception thrown on usually unrecoverable infrastructure exception.
 *
 * @author Unibet
 */
@Sealed
public class InfrastructureException extends BusinessException {
    public InfrastructureException(String message) {
        super(message);
    }
}
