package br.com.finch.api.food.util.exceptions;

public class ValidadorException extends Exception {
    public ValidadorException() {
    }

    public ValidadorException(String s) {
        super(s);
    }

    public ValidadorException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ValidadorException(Throwable throwable) {
        super(throwable);
    }

    public ValidadorException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
