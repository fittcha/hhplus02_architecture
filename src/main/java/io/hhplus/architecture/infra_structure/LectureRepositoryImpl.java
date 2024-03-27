package io.hhplus.architecture.infra_structure;

import io.hhplus.architecture.domain.lecture.entity.Lecture;
import io.hhplus.architecture.domain.lecture.repository.LectureJpaRepository;
import io.hhplus.architecture.domain.lecture.repository.LectureRepository;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;

@Repository
public class LectureRepositoryImpl implements LectureRepository {

    private final LectureJpaRepository jpaRepository;

    public LectureRepositoryImpl(LectureJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Lecture findByIdWithPessimisticLock(Long lectureId) {
        return jpaRepository.findByIdWithPessimisticLock(lectureId);
    }

    @Override
    public Lecture findById(Long lectureId) {
        return jpaRepository.findById(lectureId).orElseThrow(NoSuchElementException::new);
    }
}
