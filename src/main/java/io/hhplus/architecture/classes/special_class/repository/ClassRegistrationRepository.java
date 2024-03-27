package io.hhplus.architecture.classes.special_class.repository;

import io.hhplus.architecture.classes.special_class.domain.entity.ClassRegistration;
import io.hhplus.architecture.classes.special_class.domain.entity.SpecialClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRegistrationRepository extends JpaRepository<ClassRegistration, Long> {
    int countUserIdBySpecialClass_SpecialClassId(Long specialClassId);
    boolean existsBySpecialClassAndUserId(SpecialClass specialClass, Long userId);
}
