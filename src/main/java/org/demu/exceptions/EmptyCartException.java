package org.demu.exceptions;

public class EmptyCartException extends DemuRuntimeException {
    public EmptyCartException(String message) {
        super(message);
    }
}
