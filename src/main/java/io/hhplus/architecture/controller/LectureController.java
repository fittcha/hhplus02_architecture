package io.hhplus.architecture.controller;

import io.hhplus.architecture.domain.lecture.entity.LectureRegistration;
import io.hhplus.architecture.domain.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/lecture")
@RestController
@RequiredArgsConstructor
public class LectureController {

    private final LectureService service;

    // 특강 신청
    @PostMapping("/{userId}")
    public LectureRegistration regist(@PathVariable Long userId) {
        return service.register(userId);
    }

    // 특강 신청 여부 조회
    @GetMapping("/{userId}")
    public String check(@PathVariable(value = "userId") Long userId) {
        return service.check(userId);
    }
}
