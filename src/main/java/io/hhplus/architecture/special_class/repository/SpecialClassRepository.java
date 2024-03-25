package io.hhplus.architecture.special_class.repository;

import io.hhplus.architecture.special_class.domain.entity.SpecialClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialClassRepository extends JpaRepository<SpecialClass, Long> {
}
