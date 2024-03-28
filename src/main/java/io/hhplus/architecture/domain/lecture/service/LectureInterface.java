package io.hhplus.architecture.domain.lecture.service;

import io.hhplus.architecture.controller.dto.AddLectureRequest;
import io.hhplus.architecture.controller.dto.GetLectureResponse;
import io.hhplus.architecture.domain.lecture.entity.LectureRegistration;

import java.util.List;

public interface LectureInterface {

    // 강의 신청
    LectureRegistration register(Long userId);

    // 강의 신청 여부 조회
    String check(Long userId);

    // 강의 목록 조회
    List<GetLectureResponse> getList();

    // 강의 등록
    Long add(AddLectureRequest request);
}
