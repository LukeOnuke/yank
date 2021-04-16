package com.lukeonuke.yank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MalformedRequestException extends Exception{

    public MalformedRequestException() {
        super();
    }

    public MalformedRequestException(String message) {
        super(message);
    }
}
