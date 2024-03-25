package io.hhplus.architecture.special_class.repository;

import io.hhplus.architecture.special_class.domain.entity.Attendee;
import io.hhplus.architecture.special_class.domain.entity.SpecialClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendeeRepository extends JpaRepository<Attendee, Long> {
    int countUserIdBySpecialClass(SpecialClass specialClass);
    boolean existsBySpecialClassAndUserId(SpecialClass specialClass, Long userId);
}
