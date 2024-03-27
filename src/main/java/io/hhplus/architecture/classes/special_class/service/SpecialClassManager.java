package io.hhplus.architecture.classes.special_class.service;

import io.hhplus.architecture.classes.special_class.repository.AttendeeRepository;
import io.hhplus.architecture.classes.special_class.repository.SpecialClassRepository;
import io.hhplus.architecture.classes.special_class.domain.entity.Attendee;
import io.hhplus.architecture.classes.special_class.domain.entity.SpecialClass;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpecialClassManager {

    private final SpecialClassRepository specialClassRepository;
    private final AttendeeRepository attendeeRepository;

    // 수강 신청
    public Attendee apply(SpecialClass specialClass, Long userId) {
        // 특강 정보 업데이트 (현재 신청 인원 수)
        specialClass.updateRegister();
        // 참석자 정보 저장
        return attendeeRepository.save(new Attendee(specialClass, userId));
    }
}
