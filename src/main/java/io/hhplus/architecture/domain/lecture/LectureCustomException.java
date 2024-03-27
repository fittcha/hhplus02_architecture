package io.hhplus.architecture.domain.lecture;

import io.hhplus.architecture.MessageCommInterface;
import lombok.Getter;

public class LectureCustomException extends RuntimeException {
    @Getter
    private final String errorCode;
    private final String message;

    public LectureCustomException(MessageCommInterface messageCommInterface) {
        super(messageCommInterface.getMessage());
        this.errorCode = messageCommInterface.getCode();
        this.message = messageCommInterface.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
