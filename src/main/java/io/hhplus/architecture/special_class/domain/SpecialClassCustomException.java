package io.hhplus.architecture.special_class.domain;

import io.hhplus.architecture.MessageCommInterface;
import lombok.Getter;

public class SpecialClassCustomException extends RuntimeException {
    @Getter
    private final String errorCode;
    private final String message;

    public SpecialClassCustomException(MessageCommInterface messageCommInterface) {
        super(messageCommInterface.getMessage());
        this.errorCode = messageCommInterface.getCode();
        this.message = messageCommInterface.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
