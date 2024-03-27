package io.hhplus.architecture.domain.lecture.service;

import io.hhplus.architecture.domain.lecture.LectureCustomException;
import io.hhplus.architecture.domain.lecture.LectureExceptionEnum;
import io.hhplus.architecture.domain.lecture.entity.Lecture;
import org.springframework.stereotype.Component;

@Component
public class LectureValidator {

    public void validateRegister(Lecture lecture, Long userId) {
        // 특강 정원 초과
        if (lecture.getCurrentRegisterCnt() >= lecture.getMaxRegisterCnt()) {
            throw new LectureCustomException(LectureExceptionEnum.LECTURE_FULL);
        }

        // 이미 동일한 특강을 신청함
        boolean registerYn = lecture.getLectureRegistrationList().stream()
                .anyMatch(v -> v.getUserId().equals(userId));
        if (registerYn) {
            throw new LectureCustomException(LectureExceptionEnum.ALREADY_APPLIED);
        }
    }
}
