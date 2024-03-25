package io.hhplus.architecture.special_class;

import io.hhplus.architecture.special_class.domain.entity.Attendee;
import io.hhplus.architecture.special_class.service.SpecialClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/special-class")
@RestController
@RequiredArgsConstructor
public class SpecialClassController {

    private final SpecialClassService service;

    // 특강 신청 (비동기)
    @PostMapping("/{userId}")
    public ResponseEntity<?> apply(@PathVariable(value = "userId") Long userId) {
        service.apply(userId)
                .thenAccept(result -> {
                });
        return ResponseEntity.ok().build();
    }

    // 특강 신청 여부 조회
    @GetMapping("/{userId}")
    public String check(@PathVariable(value = "userId") Long userId) {
        return service.check(userId);
    }
}
