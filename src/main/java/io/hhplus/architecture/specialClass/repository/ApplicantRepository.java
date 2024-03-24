package io.hhplus.architecture.specialClass.repository;

import io.hhplus.architecture.specialClass.domain.entity.Applicant;
import io.hhplus.architecture.specialClass.domain.entity.SpecialClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    int countUserIdBySpecialClass(SpecialClass specialClass);
    boolean existsBySpecialClassAndUserId(SpecialClass specialClass, Long userId);
}
