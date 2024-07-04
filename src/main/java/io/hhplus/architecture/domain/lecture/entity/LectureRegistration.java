package io.hhplus.architecture.domain.lecture.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "lecture_registration")
public class LectureRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureRegistrationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, updatable = false)
    private ZonedDateTime createDatetime;

    public LectureRegistration(Lecture lecture, Long userId) {
        this.lecture = lecture;
        this.userId = userId;
    }

    @PrePersist
    protected void onCreate() {
        createDatetime = ZonedDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LectureRegistration lectureRegistration = (LectureRegistration) o;
        return lectureRegistrationId.equals(lectureRegistration.lectureRegistrationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lectureRegistrationId);
    }
}
