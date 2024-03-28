package io.hhplus.architecture.controller.dto;

import io.hhplus.architecture.domain.lecture.entity.Lecture;
import io.hhplus.architecture.domain.lecture.entity.LectureRegistration;
import lombok.Builder;

import java.time.ZonedDateTime;

public record RegisterResponse(
    Long lectureId,
    String name,
    ZonedDateTime lectureDatetime,
    Long lectureRegistrationId
) {

    @Builder
    public RegisterResponse {
    }

    public static RegisterResponse from(Lecture lecture) {
        return RegisterResponse.builder()
                .lectureId(lecture.getLectureId())
                .name(lecture.getName())
                .lectureDatetime(lecture.getLectureDatetime())
                .build();
    }
}
