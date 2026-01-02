package org.dgika.api.exception;

import java.util.List;

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

    public List<String> getMessages() {
        return messages;
    }

}
