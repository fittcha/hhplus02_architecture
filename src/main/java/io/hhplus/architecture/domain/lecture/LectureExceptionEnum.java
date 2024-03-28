package io.hhplus.architecture.domain.lecture;

import io.hhplus.architecture.MessageCommInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LectureExceptionEnum implements MessageCommInterface {

    LECTURE_FULL("LECTURE.LECTURE_FULL", "정원이 초과되어 수강 신청에 실패했습니다."),
    ALREADY_APPLIED("LECTURE.ALREADY_APPLIED", "이미 신청된 특강입니다."),
    NAME_EXIST("LECTURE.NAME_EXIST", "동일한 이름의 강의가 존재합니다."),
    DELETE_DISABLE("LECTURE.DELETE_DISABLE", "종료되거나 신청자가 없는 강의만 삭제가 가능합니다."),
    END_LECTURE("LECTURE.END_LECTURE", "이미 종료된 특강입니다.")
    ;

    private final String code;
    private final String message;
}
