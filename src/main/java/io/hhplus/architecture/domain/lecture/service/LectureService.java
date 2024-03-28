package io.hhplus.architecture.domain.lecture.service;

import io.hhplus.architecture.controller.dto.AddLectureRequest;
import io.hhplus.architecture.controller.dto.GetLectureResponse;
import io.hhplus.architecture.controller.dto.RegisterResponse;
import io.hhplus.architecture.domain.lecture.entity.Lecture;
import io.hhplus.architecture.domain.lecture.repository.LectureRegistrationRepository;
import io.hhplus.architecture.domain.lecture.repository.LectureRepository;
import jakarta.persistence.EntityNotFoundException;
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

    @Override
    @Transactional
    public RegisterResponse register(Long lectureId, Long userId) {
        // 특강 정보 조회 (비관적 락 적용)
        Lecture lecture = lectureRepository.findByIdWithPessimisticLock(lectureId);

        // validation
        lectureValidator.validateRegister(lecture, userId);

        // 특강 신청
        lecture.addRegisterCnt();
        lectureRegistrationRepository.save(lecture.getLectureId(), userId);

        return RegisterResponse.from(lecture);
    }

    @Override
    @Transactional
    public void cancel(Long lectureId, Long userId) {
        // 특강 정보 조회
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(EntityNotFoundException::new);

        // validation
        lectureValidator.validateCancel(lecture.getLectureDatetime());

        // 신청 취소
        lectureRegistrationRepository.deleteByLectureIdAndUserId(lectureId, userId);
        lecture.subRegisterCnt();
    }

    @Override
    public String check(Long lectureId, Long userId) {
        // 특강 정보 조회
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(EntityNotFoundException::new);

        // 특강 신청 내역 존재 여부 리턴
        return lecture.getLectureRegistrationList().stream()
                .anyMatch(v -> v.getUserId().equals(userId)) ? "신청 완료" : "신청 내역이 없습니다.";
    }

    @Override
    public List<GetLectureResponse> readAll() {
        List<Lecture> lectures = lectureRepository.findAll();
        return lectures.stream().map(GetLectureResponse::from).toList();
    }

    @Override
    @Transactional
    public Long add(AddLectureRequest request) {
        // validation
        lectureValidator.validateAdd(request.name());

        // 강의 등록
        Lecture lecture = lectureRepository.save(request.toEntity());
        if (lecture == null) {
            throw new RuntimeException("강의 등록 실패");
        }

        return lecture.getLectureId();
    }

    @Override
    @Transactional
    public void delete(Long lectureId) {
        // validation
        lectureValidator.validateDelete(lectureId);

        // 강의 삭제
        lectureRegistrationRepository.deleteByLectureId(lectureId);
        lectureRepository.deleteById(lectureId);
    }
}
