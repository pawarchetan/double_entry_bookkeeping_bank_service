package com.unibet.worktest.bank;

/**
 * Base type for recoverable business exceptions.
 *
 * @author Unibet
 */
@Sealed
public abstract class BusinessException extends RuntimeException {
    protected BusinessException(String message) {
        super(message);
    }
}
