package io.hhplus.architecture.domain.lecture.service;

import io.hhplus.architecture.controller.dto.AddLectureRequest;
import io.hhplus.architecture.controller.dto.GetLectureResponse;
import io.hhplus.architecture.domain.lecture.entity.LectureRegistration;
import io.hhplus.architecture.domain.lecture.entity.Lecture;
import io.hhplus.architecture.domain.lecture.repository.LectureRegistrationRepository;
import io.hhplus.architecture.domain.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureService implements LectureInterface {

    private final LectureValidator lectureValidator;
    private final LectureRepository lectureRepository;
    private final LectureRegistrationRepository lectureRegistrationRepository;

    // 현재 특강 PK : 1로 고정
    private static final Long SPC_CLASS_ID = 1L;

    @Override
    @Transactional
    public LectureRegistration register(Long userId) {
        // 특강 정보 조회 (비관적 락 적용)
        Lecture lecture = lectureRepository.findByIdWithPessimisticLock(SPC_CLASS_ID);

        // validation
        lectureValidator.validateRegister(lecture, userId);

        // 특강 신청
        lecture.addRegisterCnt();
        return lectureRegistrationRepository.add(lecture.getLectureId(), userId);
    }

    @Override
    public String check(Long userId) {
        // 특강 정보 조회
        Lecture lecture = lectureRepository.findById(SPC_CLASS_ID);

        // 특강 신청 내역 존재 여부 리턴
        return lecture.getLectureRegistrationList().stream()
                .anyMatch(v -> v.getUserId().equals(userId)) ? "신청 완료" : "신청 내역이 없습니다.";
    }

    @Override
    public List<GetLectureResponse> getList() {
        // todo
        return null;
    }

    @Override
    public Long add(AddLectureRequest request) {
        // todo
        return null;
    }
}
