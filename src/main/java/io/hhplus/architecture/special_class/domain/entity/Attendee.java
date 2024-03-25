package io.hhplus.architecture.special_class.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "applicant")
public class Attendee {

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

    public Attendee(SpecialClass specialClass, Long userId) {
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
        Attendee attendee = (Attendee) o;
        return applicantId.equals(attendee.applicantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicantId);
    }
}
