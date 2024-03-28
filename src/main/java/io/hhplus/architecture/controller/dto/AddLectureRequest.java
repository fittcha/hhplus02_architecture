package io.hhplus.architecture.controller.dto;

import io.hhplus.architecture.domain.lecture.entity.Lecture;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.ZonedDateTime;

public record AddLectureRequest(
        @NotNull String name,
        @NotNull int maxRegisterCnt,
        @NotNull ZonedDateTime lectureDatetime
) {

    @Builder
    public AddLectureRequest {
    }

    public Lecture toEntity() {
        return Lecture.builder()
                .name(name)
                .maxRegisterCnt(maxRegisterCnt)
                .lectureDatetime(lectureDatetime)
                .build();
    }
}
