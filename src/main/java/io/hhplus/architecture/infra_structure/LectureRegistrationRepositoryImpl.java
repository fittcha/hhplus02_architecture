package io.hhplus.architecture.infra_structure;

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
    public LectureRegistration save(Long lectureId, Long userId) {
        return jpaRepository.save(new LectureRegistration(lectureId, userId));
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    @Override
    public void deleteByLectureIdAndUserId(Long lectureId, Long userId) {
        jpaRepository.deleteByLectureIdAndUserId(lectureId, userId);
    }

    @Override
    public void deleteByLectureId(Long lectureId) {
        jpaRepository.deleteByLectureId(lectureId);
    }
}
