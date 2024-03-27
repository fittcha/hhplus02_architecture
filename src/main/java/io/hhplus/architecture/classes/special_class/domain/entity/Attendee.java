package io.hhplus.architecture.classes.special_class.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "attendee")
public class Attendee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendeeId;

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
        return attendeeId.equals(attendee.attendeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attendeeId);
    }
}
