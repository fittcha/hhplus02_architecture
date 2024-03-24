package io.hhplus.architecture.specialClass.service;

import io.hhplus.architecture.specialClass.domain.SpecialClassCustomException;
import org.springframework.stereotype.Component;

@Component
public class SpecialClassValidator {

    // 수강 정원 초과
    void isClassFull(int nowApplicantCnt, int maxApplicantCnt) {
        if (nowApplicantCnt >= maxApplicantCnt) {
            throw new SpecialClassCustomException("정원이 초과되어 수강 신청에 실패했습니다.");
        }
    }

    void isAlreadyApplied(boolean appliedYn) {
        if (appliedYn) {
            throw new SpecialClassCustomException("이미 신청된 특강입니다.");
        }
    }
}
