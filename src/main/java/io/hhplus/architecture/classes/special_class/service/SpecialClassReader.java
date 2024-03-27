package io.hhplus.architecture.classes.special_class.service;

import io.hhplus.architecture.classes.special_class.repository.ClassRegistrationRepository;
import io.hhplus.architecture.classes.special_class.repository.SpecialClassRepository;
import io.hhplus.architecture.classes.special_class.domain.entity.SpecialClass;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class SpecialClassReader {

    private final SpecialClassRepository specialClassRepository;
    private final ClassRegistrationRepository classRegistrationRepository;

    // 특강 조회
    public SpecialClass findById(Long specialClassId) {
        return specialClassRepository.findById(specialClassId).orElseThrow(NoSuchElementException::new);
    }

    // 수강 신청 이력 존재 여부
    public boolean existBySpecialClassAndUserId(SpecialClass specialClass, Long userId) {
        return classRegistrationRepository.existsBySpecialClassAndUserId(specialClass, userId);
    }

    // 특강 조회 - 수강 신청용 (비관적락 적용)
    public SpecialClass findByIdWithPessimisticLock(Long specialClassId) {
        return specialClassRepository.findByIdWithPessimisticLock(specialClassId);
    }
}
