package org.example.jpaentityrelationships.exceptions;

public class DuplicateResourceException extends RuntimeException{
    public  DuplicateResourceException(String message){
        super(message);
    }
}
