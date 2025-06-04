package org.demu.exceptions;

public abstract class DemuRuntimeException extends RuntimeException {
    public DemuRuntimeException(String message){
        super(message);
    }
}
