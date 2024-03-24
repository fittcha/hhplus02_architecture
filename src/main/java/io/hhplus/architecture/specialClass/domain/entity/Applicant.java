package io.hhplus.architecture.specialClass.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "applicant")
public class Applicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicantId;

    @ManyToOne
    @JoinColumn(name = "special_class_id", nullable = false)
    private SpecialClass specialClass;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createDatetime;

    public Applicant(SpecialClass specialClass, Long userId) {
        this.specialClass = specialClass;
        this.userId = userId;
    }

    @PrePersist
    protected void onCreate() {
        this.createDatetime = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Applicant applicant = (Applicant) o;
        return applicantId.equals(applicant.applicantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicantId);
    }
}
