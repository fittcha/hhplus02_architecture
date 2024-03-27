package io.hhplus.architecture.classes.special_class.repository;

import io.hhplus.architecture.classes.special_class.domain.entity.SpecialClass;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpecialClassRepository extends JpaRepository<SpecialClass, Long> {

    // 비관적 락 적용
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT sc FROM SpecialClass sc WHERE sc.specialClassId = :specialClassId")
    SpecialClass findByIdWithPessimisticLock(@Param("specialClassId") Long specialClassId);
}
