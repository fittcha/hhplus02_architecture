package io.hhplus.architecture.domain.lecture.service;

import io.hhplus.architecture.controller.dto.AddLectureRequest;
import io.hhplus.architecture.controller.dto.GetLectureResponse;
import io.hhplus.architecture.controller.dto.RegisterResponse;

import java.util.List;

public interface LectureInterface {

    // 특강 신청
    RegisterResponse register(Long lectureId, Long userId);

    // 신청 여부 조회
    String check(Long lectureId, Long userId);

    // 신청 취소
    void cancel(Long lectureId, Long userId);

    // 특강 목록 조회
    List<GetLectureResponse> readAll();

    // 특강 등록
    Long add(AddLectureRequest request);

    // 특강 삭제
    void delete(Long lectureId);
}
