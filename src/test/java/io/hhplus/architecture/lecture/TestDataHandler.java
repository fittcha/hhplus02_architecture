package io.hhplus.architecture.lecture;

import io.hhplus.architecture.domain.lecture.entity.Lecture;
import io.hhplus.architecture.domain.lecture.repository.LectureRegistrationRepository;
import io.hhplus.architecture.domain.lecture.repository.LectureRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class TestDataHandler {

    private final LectureRepository lectureRepository;
    private final LectureRegistrationRepository lectureRegistrationRepository;

    // 기존 특강 신청 테이블 초기화
    public void initRegistration() {
        lectureRegistrationRepository.deleteAll();
    }

    // 현재 특강 신청자 수 초기화
    public void initLectureCurrentRegisterCnt() {
        Lecture lecture = lectureRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
        if (lecture == null) {
            return;
        }
        lecture.initRegister();
    }

    // 특강 등록
    public void addLecture() {
        lectureRepository.save(Lecture.builder()
                .lectureId(1L)
                .name("항해 플러스 토요일 특강")
                .currentRegisterCnt(0)
                .maxRegisterCnt(30)
                .lectureDatetime(ZonedDateTime.of(LocalDateTime.of(2024, 4, 20, 13, 0, 0), ZoneId.of("Asia/Seoul")))
                .build());
    }

    // 강의 테이블 초기화
    public void initLecture() {
        lectureRepository.deleteAll();
    }
}