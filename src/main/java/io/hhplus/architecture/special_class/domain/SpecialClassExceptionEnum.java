package io.hhplus.architecture.special_class.domain;

import io.hhplus.architecture.MessageCommInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SpecialClassExceptionEnum implements MessageCommInterface {

    CLASS_FULL("SPECIAL_CLASS.CLASS_FULL", "정원이 초과되어 수강 신청에 실패했습니다."),
    ALREADY_APPLIED("SPECIAL_CLASS.ALREADY_APPLIED", "이미 신청된 특강입니다."),
    ;

    private final String code;
    private final String message;
}
