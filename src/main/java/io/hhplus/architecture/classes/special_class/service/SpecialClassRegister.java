package io.hhplus.architecture.classes.special_class.service;

import io.hhplus.architecture.classes.special_class.repository.ClassRegistrationRepository;
import io.hhplus.architecture.classes.special_class.repository.SpecialClassRepository;
import io.hhplus.architecture.classes.special_class.domain.entity.ClassRegistration;
import io.hhplus.architecture.classes.special_class.domain.entity.SpecialClass;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpecialClassRegister {

    private final SpecialClassRepository specialClassRepository;
    private final ClassRegistrationRepository classRegistrationRepository;

    // 수강 신청
    public ClassRegistration regist(SpecialClass specialClass, Long userId) {
        // 특강 정보 업데이트 (현재 신청 인원 수)
        specialClass.updateRegister();
        // 특강 신청 정보 저장
        return classRegistrationRepository.save(new ClassRegistration(specialClass, userId));
    }
}
