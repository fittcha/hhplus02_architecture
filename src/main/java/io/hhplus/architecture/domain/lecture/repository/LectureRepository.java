package io.hhplus.architecture.domain.lecture.repository;

import io.hhplus.architecture.domain.lecture.entity.Lecture;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository {

    Lecture findByIdWithPessimisticLock(@Param("lectureId") Long lectureId);

    Lecture findById(Long lectureId);

    Lecture save(Lecture lecture);

    List<Lecture> findAll();

    Optional<Lecture> findByName(String name);
}
