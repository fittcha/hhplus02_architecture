package io.hhplus.architecture.specialClass.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ApplicantEnums {

    @Getter
    @RequiredArgsConstructor
    public enum Status {
        READY("APPLICANT.STATUS.READY", "대기중"),
        COMPLETE("APPLICANT.STATUS.COMPLETE", "완료");

        private final String code;
        private final String codeName;

        // TODO
    }
}
