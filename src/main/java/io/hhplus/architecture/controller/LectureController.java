package io.hhplus.architecture.controller;

import io.hhplus.architecture.controller.dto.AddLectureRequest;
import io.hhplus.architecture.controller.dto.GetLectureResponse;
import io.hhplus.architecture.controller.dto.RegisterResponse;
import io.hhplus.architecture.controller.dto.UserIdRequest;
import io.hhplus.architecture.domain.lecture.service.LectureService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/lectures")
@RestController
@RequiredArgsConstructor
public class LectureController {

    private final LectureService service;

    // 특강 신청
    @PostMapping("/{lectureId}")
    public RegisterResponse register(@PathVariable(value = "lectureId") Long lectureId, @Validated @RequestBody UserIdRequest request) {
        return service.register(lectureId, request.userId());
    }

    // 특강 신청 여부 조회
    @GetMapping("/{lectureId}/application")
    public String check(@PathVariable(value = "lectureId") Long lectureId, @NotNull @RequestParam(value = "userId") Long userId) {
        return service.check(lectureId, userId);
    }

    // 신청 취소
    @PostMapping("/{lectureId}/cancel")
    public void cancel(@PathVariable(value = "lectureId") Long lectureId, @Validated @RequestBody UserIdRequest request) {
        service.cancel(lectureId, request.userId());
    }

    // 특강 목록 조회
    @GetMapping("/")
    public List<GetLectureResponse> readAll() {
        return service.readAll();
    }

    // 강의 등록
    @PostMapping("/")
    public Long add(@Validated @RequestBody AddLectureRequest request) {
        return service.add(request);
    }

    // 강의 삭제
    @DeleteMapping("/{lectureId}")
    public void delete(@PathVariable(value = "lectureId") Long lectureId) {
        service.delete(lectureId);
    }
}
