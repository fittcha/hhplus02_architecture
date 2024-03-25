package io.hhplus.architecture.special_class.service;

import io.hhplus.architecture.special_class.domain.entity.SpecialClass;
import io.hhplus.architecture.special_class.repository.AttendeeRepository;
import io.hhplus.architecture.special_class.repository.SpecialClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class SpecialClassReader {

    private final SpecialClassRepository specialClassRepository;
    private final AttendeeRepository attendeeRepository;


    // 특강 조회
    public SpecialClass findById(Long specialClassId) {
        return specialClassRepository.findById(specialClassId).orElseThrow(NoSuchElementException::new);
    }

    // 수강 신청 이력 존재 여부
    public boolean existBySpecialClassAndUserId(SpecialClass specialClass, Long userId) {
        return attendeeRepository.existsBySpecialClassAndUserId(specialClass, userId);
    }
}
