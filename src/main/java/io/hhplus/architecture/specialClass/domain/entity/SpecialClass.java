package io.hhplus.architecture.specialClass.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "specialClass")
public class SpecialClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long specialClassId;

    @Column(nullable = false)
    private String name;

    private int maxApplicantCnt;

    @Column(nullable = false)
    private LocalDateTime classDatetime;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createDatetime;

    @Builder
    public SpecialClass(Long specialClassId, String name, int maxApplicantCnt, LocalDateTime classDatetime) {
        this.specialClassId = specialClassId;
        this.name = name;
        this.maxApplicantCnt = maxApplicantCnt;
        this.classDatetime = classDatetime;
    }

    @PrePersist
    protected void onCreate() {
        this.createDatetime = LocalDateTime.now();
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
