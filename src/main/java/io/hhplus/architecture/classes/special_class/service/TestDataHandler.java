package io.hhplus.architecture.classes.special_class.service;

import io.hhplus.architecture.classes.special_class.domain.entity.SpecialClass;
import io.hhplus.architecture.classes.special_class.repository.ClassRegistrationRepository;
import io.hhplus.architecture.classes.special_class.repository.SpecialClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TestDataHandler {

    private final SpecialClassRepository specialClassRepository;
    private final ClassRegistrationRepository classRegistrationRepository;

    // 기존 특강 신청 테이블 초기화
    @Transactional
    public void initAttendee() {
        classRegistrationRepository.deleteAll();
    }

    // 현재 특강 신청자 수 초기화
    @Transactional
    public void initSpecialClassNowRegisterCnt() {
        SpecialClass specialClass = specialClassRepository.findById(1L).orElse(null);
        if (specialClass == null) {
            return;
        }
        specialClass.initRegister();
    }
}
