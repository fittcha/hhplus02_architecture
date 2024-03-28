package io.hhplus.architecture.controller;

import io.hhplus.architecture.controller.dto.*;
import io.hhplus.architecture.domain.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/lecture")
@RestController
@RequiredArgsConstructor
public class LectureController {

    private final LectureService service;

    // 특강 신청
    @PostMapping("/{lectureId}")
    public RegisterResponse register(@PathVariable(value = "lectureId") Long lectureId, RegisterRequest request) {
        return service.register(lectureId, request.userId());
    }

    // 특강 신청 여부 조회
    @GetMapping("/{lectureId}")
    public String check(@PathVariable(value = "lectureId") Long lectureId,
                        @RequestParam(value = "userId") Long userId) {
        return service.check(lectureId, userId);
    }

    // 특강 목록 조회
    @GetMapping("/")
    public List<GetLectureResponse> readAll() {
        return service.readAll();
    }

    // 특강 등록
    @PostMapping("/")
    public Long add(@Validated @RequestBody AddLectureRequest request) {
        return service.add(request);
    }
}
