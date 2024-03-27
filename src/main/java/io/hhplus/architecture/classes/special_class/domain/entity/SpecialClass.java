package io.hhplus.architecture.classes.special_class.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

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

    @Column(nullable = false, updatable = false)
    private LocalDateTime createDatetime;

    @Builder
    public SpecialClass(Long specialClassId, String name, int nowRegisterCnt, int maxRegisterCnt, LocalDateTime classDatetime) {
        this.specialClassId = specialClassId;
        this.name = name;
        this.nowRegisterCnt = nowRegisterCnt;
        this.maxRegisterCnt = maxRegisterCnt;
        this.classDatetime = classDatetime;
    }

    @PrePersist
    protected void onCreate() {
        this.createDatetime = LocalDateTime.now();
    }

    public void updateRegister() {
        this.nowRegisterCnt = this.nowRegisterCnt + 1;
    }

    public void initRegister() {
        this.nowRegisterCnt = 0;
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
