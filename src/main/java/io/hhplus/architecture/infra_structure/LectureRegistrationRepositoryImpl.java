package io.hhplus.architecture.infra_structure;

import io.hhplus.architecture.domain.lecture.entity.Lecture;
import io.hhplus.architecture.domain.lecture.entity.LectureRegistration;
import io.hhplus.architecture.domain.lecture.repository.LectureRegistrationRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LectureRegistrationRepositoryImpl implements LectureRegistrationRepository {

    private final LectureRegistrationJpaRepository jpaRepository;

    public LectureRegistrationRepositoryImpl(LectureRegistrationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public LectureRegistration save(Lecture lecture, Long userId) {
        return jpaRepository.save(new LectureRegistration(lecture, userId));
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    @Override
    public void deleteByLectureAndUserId(Lecture lecture, Long userId) {
        jpaRepository.deleteByLectureAndUserId(lecture, userId);
    }

    @Override
    public void deleteByLecture(Lecture lecture) {
        jpaRepository.deleteByLecture(lecture);
    }
}
