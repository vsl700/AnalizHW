package org.demu.exceptions;

public class InsufficientBalanceException extends DemuRuntimeException {
    public InsufficientBalanceException(String message){
        super(message);
    }
}
