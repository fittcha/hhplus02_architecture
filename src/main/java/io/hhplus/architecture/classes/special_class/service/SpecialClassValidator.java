package io.hhplus.architecture.classes.special_class.service;

import io.hhplus.architecture.classes.special_class.domain.SpecialClassCustomException;
import io.hhplus.architecture.classes.special_class.domain.SpecialClassExceptionEnum;
import org.springframework.stereotype.Component;

@Component
public class SpecialClassValidator {

    // 수강 정원 초과
    void isClassFull(int nowRegisterCnt, int maxRegisterCnt) {
        if (nowRegisterCnt >= maxRegisterCnt) {
            throw new SpecialClassCustomException(SpecialClassExceptionEnum.CLASS_FULL);
        }
    }

    void isAlreadyApplied(boolean appliedYn) {
        if (appliedYn) {
            throw new SpecialClassCustomException(SpecialClassExceptionEnum.ALREADY_APPLIED);
        }
    }
}
