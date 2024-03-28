package io.hhplus.architecture.domain.lecture.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

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

    @Column(name = "lecture_id")
    private Long lectureId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, updatable = false)
    private ZonedDateTime createDatetime;

    public LectureRegistration(Long lectureId, Long userId) {
        this.lectureId = lectureId;
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
