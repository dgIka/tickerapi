package org.dgika.api.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class BadRequestException extends RuntimeException{

    private final List<String> messages;

    public BadRequestException(String message) {
        super(message);
        this.messages = List.of(message);
    }

    public BadRequestException(List<String> messages) {
        super(String.join("; ", messages));
        this.messages = messages;
    }

}
