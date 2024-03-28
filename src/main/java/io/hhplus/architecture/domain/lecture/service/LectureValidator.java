package io.hhplus.architecture.domain.lecture.service;

import io.hhplus.architecture.controller.dto.AddLectureRequest;
import io.hhplus.architecture.domain.lecture.LectureCustomException;
import io.hhplus.architecture.domain.lecture.LectureExceptionEnum;
import io.hhplus.architecture.domain.lecture.entity.Lecture;
import io.hhplus.architecture.domain.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LectureValidator {

    private final LectureRepository lectureRepository;

    public void validateRegister(Lecture lecture, Long userId) {
        // 특강 정원 초과
        if (lecture.getCurrentRegisterCnt() >= lecture.getMaxRegisterCnt()) {
            throw new LectureCustomException(LectureExceptionEnum.LECTURE_FULL);
        }

        // 이미 동일한 특강을 신청함
        boolean isExist = lecture.getLectureRegistrationList().stream()
                .anyMatch(v -> v.getUserId().equals(userId));
        if (isExist) {
            throw new LectureCustomException(LectureExceptionEnum.ALREADY_APPLIED);
        }
    }

    public void validateAdd(AddLectureRequest request) {
        // 동일한 이름의 특강 존재
        boolean isExist = lectureRepository.findByName(request.name()).isPresent();
        if (isExist) {
            throw new LectureCustomException(LectureExceptionEnum.NAME_EXIST);
        }
    }
}
