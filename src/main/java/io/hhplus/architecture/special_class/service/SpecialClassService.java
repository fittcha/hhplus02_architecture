package io.hhplus.architecture.special_class.service;

import io.hhplus.architecture.special_class.domain.entity.Attendee;
import io.hhplus.architecture.special_class.domain.entity.SpecialClass;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecialClassService implements SpecialClassInterface {

    private final SpecialClassManager specialClassManager;
    private final SpecialClassReader specialClassReader;
    private final SpecialClassValidator specialClassValidator;

    // 현재 특강 PK : 1로 고정
    private static final Long SPC_CLASS_ID = 1L;

    @Override
    public Attendee apply(Long userId) {
        // 특강 정보 조회
        SpecialClass specialClass = specialClassReader.findById(SPC_CLASS_ID);

        // validate - 이미 동일한 특강을 신청함
        specialClassValidator.isAlreadyApplied(specialClassReader.existBySpecialClassAndUserId(specialClass, userId));

        // validate - 특강 정원 초과
        specialClassValidator.isClassFull(specialClass.getNowRegisterCnt(), specialClass.getMaxRegisterCnt());

        // 특강 신청
        return specialClassManager.apply(specialClass, userId);
    }

    @Override
    public boolean check(Long userId) {
        // 특강 정보 조회
        SpecialClass specialClass = specialClassReader.findById(SPC_CLASS_ID);

        // 특강 신청 내역 존재 여부 리턴
        return specialClassReader.existBySpecialClassAndUserId(specialClass, userId);
    }
}
