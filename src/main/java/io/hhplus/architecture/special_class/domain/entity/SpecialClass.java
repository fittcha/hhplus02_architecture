package io.hhplus.architecture.special_class.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@RequiredArgsConstructor
@DynamicUpdate
@Table(name = "specialClass")
public class SpecialClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long specialClassId;

    @Column(nullable = false, length = 25)
    private String name;

    @Column(nullable = false)
    private int nowRegisterCnt;

    private int maxRegisterCnt;

    @Column(nullable = false)
    private LocalDateTime classDatetime;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createDatetime;

    // 낙관적 락 (Optimistic Locking) 버전 관리
    @Version
    private Long version;

    @Builder
    public SpecialClass(Long specialClassId, String name, int nowRegisterCnt, int maxRegisterCnt, LocalDateTime classDatetime) {
        this.specialClassId = specialClassId;
        this.name = name;
        this.nowRegisterCnt = nowRegisterCnt;
        this.maxRegisterCnt = maxRegisterCnt;
        this.classDatetime = classDatetime;
    }

    public void updateRegister() {
        this.nowRegisterCnt = this.nowRegisterCnt + 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecialClass that = (SpecialClass) o;
        return Objects.equals(specialClassId, that.specialClassId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(specialClassId);
    }
}
