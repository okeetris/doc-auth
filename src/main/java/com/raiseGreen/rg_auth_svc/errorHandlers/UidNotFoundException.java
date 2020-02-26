package com.raiseGreen.rg_auth_svc.errorHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UidNotFoundException extends RuntimeException {
    public UidNotFoundException(String message) {
        super(message);
    }
}
