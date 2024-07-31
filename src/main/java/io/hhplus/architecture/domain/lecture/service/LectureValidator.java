package io.hhplus.architecture.domain.lecture.service;

import io.hhplus.architecture.domain.lecture.LectureCustomException;
import io.hhplus.architecture.domain.lecture.LectureExceptionEnum;
import io.hhplus.architecture.domain.lecture.entity.Lecture;
import io.hhplus.architecture.domain.lecture.entity.LectureRegistration;
import io.hhplus.architecture.domain.lecture.repository.LectureRegistrationRepository;
import io.hhplus.architecture.domain.lecture.repository.LectureRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class LectureValidator {

    private final LectureRepository lectureRepository;
    private final LectureRegistrationRepository lectureRegistrationRepository;

    public void validateRegister(Lecture lecture, Long userId) {
        // 특강 정원 초과
        if (lecture.getCurrentRegisterCnt() >= lecture.getMaxRegisterCnt()) {
            throw new LectureCustomException(LectureExceptionEnum.LECTURE_FULL);
        }

        // 이미 동일한 특강을 신청함
        LectureRegistration lectureRegistration = lectureRegistrationRepository.findByLectureAndUserId(lecture, userId);
        if (lectureRegistration != null) {
            throw new LectureCustomException(LectureExceptionEnum.ALREADY_APPLIED);
        }
    }

    public void validateAdd(String name) {
        // 동일한 이름의 특강 존재
        if (lectureRepository.findByName(name).isPresent()) {
            throw new LectureCustomException(LectureExceptionEnum.NAME_EXIST);
        }
    }

    public void validateDelete(Long lectureId) {
        // 특강 날짜가 지나지 않았고 신청자가 존재하면 삭제 불가
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(EntityNotFoundException::new);
        LectureRegistration lectureRegistration = lectureRegistrationRepository.findFirstByLecture(lecture);
        if (lectureRegistration != null) {
            throw new LectureCustomException(LectureExceptionEnum.DELETE_DISABLE);
        }
    }

    public void validateCancel(ZonedDateTime lectureDatetime) {
        // 특강이 이미 진행되었다면 취소 불가
        if (lectureDatetime.isBefore(ZonedDateTime.now())) {
            throw new LectureCustomException(LectureExceptionEnum.END_LECTURE);
        }
    }
}
