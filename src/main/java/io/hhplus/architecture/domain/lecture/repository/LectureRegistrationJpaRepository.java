package io.hhplus.architecture.domain.lecture.repository;

import io.hhplus.architecture.domain.lecture.entity.LectureRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRegistrationJpaRepository extends JpaRepository<LectureRegistration, Long> {
    void deleteByLectureIdAndUserId(Long lectureId, Long userId);

    void deleteByLectureId(Long lectureId);
}
