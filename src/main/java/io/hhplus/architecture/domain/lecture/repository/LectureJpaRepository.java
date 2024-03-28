package io.hhplus.architecture.domain.lecture.repository;

import io.hhplus.architecture.domain.lecture.entity.Lecture;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE) // 비관적 락 적용
    @Query("SELECT lt FROM Lecture lt WHERE lt.lectureId = :lectureId")
    Lecture findByIdWithPessimisticLock(@Param("lectureId") Long lectureId);

    Optional<Lecture> findByName(String name);
}
