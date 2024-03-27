package io.hhplus.architecture.classes.special_class.repository;

import io.hhplus.architecture.classes.special_class.domain.entity.Attendee;
import io.hhplus.architecture.classes.special_class.domain.entity.SpecialClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendeeRepository extends JpaRepository<Attendee, Long> {
    int countUserIdBySpecialClass_SpecialClassId(Long specialClassId);
    boolean existsBySpecialClassAndUserId(SpecialClass specialClass, Long userId);
}
