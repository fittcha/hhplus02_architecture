package io.hhplus.architecture.special_class;

import io.hhplus.architecture.special_class.domain.entity.Attendee;
import io.hhplus.architecture.special_class.service.SpecialClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/special-class")
@RestController
@RequiredArgsConstructor
public class SpecialClassController {

    private final SpecialClassService service;

    // 특강 신청
    @PostMapping("/{userId}")
    public Attendee apply(@PathVariable Long userId) {
        return service.apply(userId);
    }

    // 특강 신청 여부 조회
    @GetMapping("/{userId}")
    public boolean check(@PathVariable Long userId) {
        return service.check(userId);
    }
}
