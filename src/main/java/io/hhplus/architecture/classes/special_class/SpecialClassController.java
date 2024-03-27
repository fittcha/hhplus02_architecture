package io.hhplus.architecture.classes.special_class;

import io.hhplus.architecture.classes.special_class.domain.entity.Attendee;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;



import io.hhplus.architecture.classes.special_class.service.SpecialClassService;

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
    public String check(@PathVariable(value = "userId") Long userId) {
        return service.check(userId);
    }
}
