package io.hhplus.architecture.domain.lecture.repository;

import io.hhplus.architecture.domain.lecture.entity.Lecture;
import io.hhplus.architecture.domain.lecture.entity.LectureRegistration;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRegistrationRepository {

    LectureRegistration save(Lecture lecture, Long userId);

    void deleteAll();

    void deleteByLectureAndUserId(Lecture lecture, Long userId);

    void deleteByLecture(Lecture lecture);

    LectureRegistration findByLectureAndUserId(Lecture lecture, Long userId);

    LectureRegistration findFirstByLecture(Lecture lecture);
}
