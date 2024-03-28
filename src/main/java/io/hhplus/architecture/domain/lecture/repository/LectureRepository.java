package io.hhplus.architecture.domain.lecture.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository {

    Lecture findByIdWithPessimisticLock(@Param("lectureId") Long lectureId);

    Lecture findById(Long lectureId);
}
