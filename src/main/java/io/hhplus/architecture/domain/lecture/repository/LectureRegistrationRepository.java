package io.hhplus.architecture.domain.lecture.repository;

import io.hhplus.architecture.domain.lecture.entity.LectureRegistration;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRegistrationRepository {

    LectureRegistration save(Long lectureId, Long userId);

    void deleteAll();

    void deleteByLectureIdAndUserId(Long lectureId, Long userId);

    void deleteByLectureId(Long lectureId);
}
