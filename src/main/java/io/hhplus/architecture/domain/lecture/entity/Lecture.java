package io.hhplus.architecture.domain.lecture.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "lecture")
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureId;

    @Column(nullable = false, length = 25)
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "lecture_id")
    private List<LectureRegistration> lectureRegistrationList = new ArrayList();

    @Column(nullable = false)
    private int currentRegisterCnt = 0;

    private int maxRegisterCnt;

    @Column(nullable = false)
    private ZonedDateTime lectureDatetime;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private ZonedDateTime createDatetime;

    @LastModifiedDate
    private ZonedDateTime updateDatetime;

    @Builder
    public Lecture(Long lectureId, String name, List<LectureRegistration> lectureRegistrationList, int currentRegisterCnt, int maxRegisterCnt, ZonedDateTime lectureDatetime) {
        this.lectureId = lectureId;
        this.name = name;
        this.maxRegisterCnt = maxRegisterCnt;
        this.lectureDatetime = lectureDatetime;
    }


    public void addRegisterCnt() {
        this.currentRegisterCnt = this.currentRegisterCnt + 1;
    }

    public void initRegister() {
        this.currentRegisterCnt = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lecture that = (Lecture) o;
        return Objects.equals(lectureId, that.lectureId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lectureId);
    }
}
