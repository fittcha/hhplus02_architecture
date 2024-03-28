package io.hhplus.architecture.infra_structure;

import io.hhplus.architecture.domain.lecture.entity.Lecture;
import io.hhplus.architecture.domain.lecture.repository.LectureJpaRepository;
import io.hhplus.architecture.domain.lecture.repository.LectureRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    @Override
    public Lecture save(Lecture lecture) {
        return jpaRepository.save(lecture);
    }

    @Override
    public List<Lecture> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<Lecture> findByName(String name) {
        return jpaRepository.findByName(name);
    }
}
