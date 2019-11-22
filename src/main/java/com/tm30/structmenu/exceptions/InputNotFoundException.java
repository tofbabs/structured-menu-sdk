package com.tm30.structmenu.exceptions;

public class InputNotFoundException extends Exception {

    public InputNotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
