package io.hhplus.architecture.classes.lecture;

import io.hhplus.architecture.domain.lecture.entity.Lecture;
import io.hhplus.architecture.domain.lecture.repository.LectureRegistrationRepository;
import io.hhplus.architecture.domain.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TestDataHandler {

    private final LectureRepository lectureRepository;
    private final LectureRegistrationRepository lectureRegistrationRepository;

    // 기존 특강 신청 테이블 초기화
    @Transactional
    public void initRegistration() {
        lectureRegistrationRepository.deleteAll();
    }

    // 현재 특강 신청자 수 초기화
    @Transactional
    public void initLectureCurrentRegisterCnt() {
        Lecture lecture = lectureRepository.findById(1L);
        if (lecture == null) {
            return;
        }
        lecture.initRegister();
    }
}