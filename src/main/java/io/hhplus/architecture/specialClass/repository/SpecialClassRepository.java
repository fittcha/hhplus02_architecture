package io.hhplus.architecture.specialClass.repository;

import io.hhplus.architecture.specialClass.domain.entity.SpecialClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialClassRepository extends JpaRepository<SpecialClass, Long> {
}
