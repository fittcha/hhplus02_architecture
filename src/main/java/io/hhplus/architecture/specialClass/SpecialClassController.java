package io.hhplus.architecture.specialClass;

import io.hhplus.architecture.specialClass.domain.entity.Applicant;
import io.hhplus.architecture.specialClass.service.SpecialClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/special-class")
@RestController
@RequiredArgsConstructor
public class SpecialClassController {

    private final SpecialClassService service;

    @PostMapping("/{userId}")
    public Applicant apply(@PathVariable Long userId) {
        return service.apply(userId);
    }

    @GetMapping("/{userId}")
    public boolean check(@PathVariable Long userId) {
        return service.check(userId);
    }
}
