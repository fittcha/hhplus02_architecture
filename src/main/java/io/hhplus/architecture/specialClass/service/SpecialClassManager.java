package io.hhplus.architecture.specialClass.service;

import io.hhplus.architecture.specialClass.domain.entity.Applicant;
import io.hhplus.architecture.specialClass.domain.entity.SpecialClass;
import io.hhplus.architecture.specialClass.repository.ApplicantRepository;
import io.hhplus.architecture.specialClass.repository.SpecialClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpecialClassManager {

    private final SpecialClassRepository specialClassRepository;
    private final ApplicantRepository applicantRepository;

    // 수강 신청
    public Applicant apply(SpecialClass specialClass, Long userId) {
        return applicantRepository.save(new Applicant(specialClass, userId));
    }
}
