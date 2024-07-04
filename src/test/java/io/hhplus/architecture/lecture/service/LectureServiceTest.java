package io.hhplus.architecture.lecture.service;

import io.hhplus.architecture.controller.dto.AddLectureRequest;
import io.hhplus.architecture.controller.dto.RegisterResponse;
import io.hhplus.architecture.domain.lecture.LectureCustomException;
import io.hhplus.architecture.domain.lecture.LectureExceptionEnum;
import io.hhplus.architecture.domain.lecture.entity.Lecture;
import io.hhplus.architecture.domain.lecture.entity.LectureRegistration;
import io.hhplus.architecture.domain.lecture.repository.LectureRegistrationRepository;
import io.hhplus.architecture.domain.lecture.repository.LectureRepository;
import io.hhplus.architecture.domain.lecture.service.LectureService;
import io.hhplus.architecture.domain.lecture.service.LectureValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class LectureServiceTest {

    private LectureService lectureService;
    private LectureRepository lectureRepository;
    private LectureRegistrationRepository lectureRegistrationRepository;
    private LectureValidator lectureValidator;
    private Lecture 항해_플러스_특강;

    @BeforeEach
    void setUp() {
        // mocking
        lectureRepository = Mockito.mock(LectureRepository.class);
        lectureRegistrationRepository = Mockito.mock(LectureRegistrationRepository.class);
        lectureValidator = Mockito.mock(LectureValidator.class);
        lectureService = new LectureService(
                lectureValidator,
                lectureRepository,
                lectureRegistrationRepository
        );

        // 항해 플러스 토요일 특강 세팅
        항해_플러스_특강 = Lecture.builder()
                .lectureId(1L)
                .name("항해 플러스 토요일 특강")
                .currentRegisterCnt(0)
                .maxRegisterCnt(30)
                .lectureDatetime(ZonedDateTime.of(LocalDateTime.of(2024, 4, 20, 13, 0, 0), ZoneId.of("Asia/Seoul")))
                .build();
    }

    @Test
    @DisplayName("해당 특강의 신청자가 30명 이상이면 신청 불가")
    void failWhenLectureIsFull() {
        // given
        Long userId = 11L;
        Lecture 항해_플러스_특강_FULL = Lecture.builder()
                .lectureId(2L)
                .name("꽉 찬 항해 플러스 특강")
                .currentRegisterCnt(30)
                .maxRegisterCnt(30)
                .lectureDatetime(ZonedDateTime.of(LocalDateTime.of(2024, 4, 20, 13, 0, 0), ZoneId.of("Asia/Seoul")))
                .build();

        // when
        doThrow(new LectureCustomException(LectureExceptionEnum.LECTURE_FULL)).when(lectureValidator).validateRegister(any(), anyLong());

        // then
        LectureCustomException expected = assertThrows(LectureCustomException.class, () ->
                lectureValidator.validateRegister(항해_플러스_특강_FULL, userId));
        assertThat(expected.getMessage()).isEqualTo("정원이 초과되어 수강 신청에 실패했습니다.");
    }

    @Test
    @DisplayName("특강 신청 성공")
    void successWhenLectureIsAvailable() {
        // given
        Long lectureId = 1L;
        Long userId = 11L;

        // when
        when(lectureRepository.findByIdWithPessimisticLock(1L)).thenReturn(항해_플러스_특강);
        when(lectureRegistrationRepository.save(any(), anyLong())).thenReturn(new LectureRegistration(
                항해_플러스_특강,
                userId
        ));
        RegisterResponse response = lectureService.register(lectureId, userId);

        // then
        assertNotNull(response);
        assertEquals(response.lectureId(), 1L);
        assertEquals(response.name(), "항해 플러스 토요일 특강");
    }

    @Test
    @DisplayName("특강 신청을 실패하면 유저에게 실패했음을 반환")
    void returnWhenLectureIsNotAvailable() {
        // given
        Long lectureId = 1L;
        Long userId = 2L;

        // when
        when(lectureRepository.findById(1L)).thenReturn(Optional.ofNullable(항해_플러스_특강));
        String registerResult = lectureService.check(lectureId, userId);

        // then
        assertEquals(registerResult, "신청 내역이 없습니다.");
    }

    @Test
    @DisplayName("동일한 이름의 특강은 등록 불가")
    void failAddLectureWhenAlreadyExistLectureName() {
        // given
        String name = "항해 플러스 토요일 특강";

        // when
        when(lectureRepository.findByName(name)).thenReturn(Optional.ofNullable(항해_플러스_특강));
        doThrow(new LectureCustomException(LectureExceptionEnum.NAME_EXIST)).when(lectureValidator).validateAdd(anyString());

        // then
        LectureCustomException expected = assertThrows(LectureCustomException.class, () ->
                lectureService.add(AddLectureRequest.builder().name(name).build()));
        assertThat(expected.getMessage()).isEqualTo("동일한 이름의 강의가 존재합니다.");
    }

    @Test
    @DisplayName("특강 날짜가 지나지 않았거나 신청자가 존재하면 특강 삭제 불가")
    void failDeleteLectureWhenNotEndOrApplierExist() {
        // given
        Long lectureId = 1L;

        // when
        doThrow(new LectureCustomException(LectureExceptionEnum.DELETE_DISABLE)).when(lectureValidator).validateDelete(anyLong());

        // then
        LectureCustomException expected = assertThrows(LectureCustomException.class, () ->
                lectureService.delete(lectureId));
        assertThat(expected.getMessage()).isEqualTo("종료되거나 신청자가 없는 강의만 삭제가 가능합니다.");
    }
}