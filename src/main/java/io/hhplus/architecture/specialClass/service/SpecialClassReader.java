package io.hhplus.architecture.specialClass.service;

import io.hhplus.architecture.specialClass.domain.entity.SpecialClass;
import io.hhplus.architecture.specialClass.repository.ApplicantRepository;
import io.hhplus.architecture.specialClass.repository.SpecialClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class SpecialClassReader {

    private final SpecialClassRepository specialClassRepository;
    private final ApplicantRepository applicantRepository;


    // 특강 조회
    public SpecialClass findById(Long specialClassId) {
        return specialClassRepository.findById(specialClassId).orElseThrow(NoSuchElementException::new);
    }

    // 수강 인원 조회
    public int countUserByClass(SpecialClass specialClass) {
        return applicantRepository.countUserIdBySpecialClass(specialClass);
    }

    // 수강 신청 이력 존재 여부
    public boolean existBySpecialClassAndUserId(SpecialClass specialClass, Long userId) {
        return applicantRepository.existsBySpecialClassAndUserId(specialClass, userId);
    }
}
