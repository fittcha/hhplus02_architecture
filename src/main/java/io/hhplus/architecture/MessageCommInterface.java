package io.hhplus.architecture;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = MessageCommSerializer.class)
public interface MessageCommInterface {
    String getCode();
    String getMessage();
}
