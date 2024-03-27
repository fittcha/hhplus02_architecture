package io.hhplus.architecture.domain.lecture.repository;

import io.hhplus.architecture.domain.lecture.entity.LectureRegistration;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRegistrationRepository {

    LectureRegistration add(Long lectureId, Long userId);

    void deleteAll();
}
