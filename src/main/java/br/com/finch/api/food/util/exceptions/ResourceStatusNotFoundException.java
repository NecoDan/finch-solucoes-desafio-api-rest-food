package br.com.finch.api.food.util.exceptions;

public class ResourceStatusNotFoundException extends RuntimeException {
    public ResourceStatusNotFoundException(String s) {
        super("Resource: ".concat(s).concat(" not found."));
    }
}
