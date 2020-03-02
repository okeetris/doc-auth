package com.raiseGreen.rg_auth_svc.errorHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UidNotPresentException extends RuntimeException {
    public UidNotPresentException(String message) {
        super(message);
    }
}
