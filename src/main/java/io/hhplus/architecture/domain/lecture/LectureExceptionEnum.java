package io.hhplus.architecture.domain.lecture;

import io.hhplus.architecture.MessageCommInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LectureExceptionEnum implements MessageCommInterface {

    LECTURE_FULL("LECTURE.LECTURE_FULL", "정원이 초과되어 수강 신청에 실패했습니다."),
    ALREADY_APPLIED("LECTURE.ALREADY_APPLIED", "이미 신청된 특강입니다."),
    ;

    private final String code;
    private final String message;
}
