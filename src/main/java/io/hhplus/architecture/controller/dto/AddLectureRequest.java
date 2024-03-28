package io.hhplus.architecture.controller.dto;

import io.hhplus.architecture.domain.lecture.entity.Lecture;

import java.time.ZonedDateTime;

public record AddLectureRequest(
    String name,
    int maxRegisterCnt,
    ZonedDateTime lectureDatetime
) {
    public Lecture toEntity() {
        return Lecture.builder()
                .name(name)
                .maxRegisterCnt(maxRegisterCnt)
                .lectureDatetime(lectureDatetime)
                .build();
    }
}
