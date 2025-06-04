package org.demu.exceptions;

public class RecordExistsException extends DemuRuntimeException {
    public RecordExistsException(String message){
        super(message);
    }
}
