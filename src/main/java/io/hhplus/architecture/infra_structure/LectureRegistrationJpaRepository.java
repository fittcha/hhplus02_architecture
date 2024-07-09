package io.hhplus.architecture.infra_structure;

import io.hhplus.architecture.domain.lecture.entity.Lecture;
import io.hhplus.architecture.domain.lecture.entity.LectureRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRegistrationJpaRepository extends JpaRepository<LectureRegistration, Long> {
    void deleteByLectureAndUserId(Lecture lecture, Long userId);

    void deleteByLecture(Lecture lecture);

    LectureRegistration findByLectureAndUserId(Lecture lecture, Long userId);
}
