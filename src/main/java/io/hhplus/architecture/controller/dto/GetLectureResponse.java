package io.hhplus.architecture.controller.dto;

import io.hhplus.architecture.domain.lecture.entity.Lecture;
import lombok.Builder;

import java.time.ZonedDateTime;

public record GetLectureResponse(
        Long lectureId,
        String name,
        int currentRegisterCnt,
        int maxRegisterCnt,
        ZonedDateTime lectureDatetime,
        ZonedDateTime createDatetime
) {

    @Builder
    public GetLectureResponse {
    }

    public static GetLectureResponse from(Lecture lecture) {
        return GetLectureResponse.builder()
                .lectureId(lecture.getLectureId())
                .name(lecture.getName())
                .currentRegisterCnt(lecture.getCurrentRegisterCnt())
                .maxRegisterCnt(lecture.getMaxRegisterCnt())
                .lectureDatetime(lecture.getLectureDatetime())
                .createDatetime(lecture.getCreateDatetime())
                .build();
    }
}
