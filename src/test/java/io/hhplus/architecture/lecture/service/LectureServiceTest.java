package io.hhplus.architecture.lecture.service;

import io.hhplus.architecture.controller.dto.AddLectureRequest;
import io.hhplus.architecture.controller.dto.RegisterResponse;
import io.hhplus.architecture.domain.lecture.LectureExceptionEnum;
import io.hhplus.architecture.domain.lecture.entity.LectureRegistration;
import io.hhplus.architecture.domain.lecture.LectureCustomException;
import io.hhplus.architecture.domain.lecture.entity.Lecture;
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
    @DisplayName("신청자가_30명_이상이면_신청_불가")
    void registerTest_신청자가_30명_이상이면_신청_불가() {
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
    @DisplayName("신청_성공")
    void registerTest_신청_성공() {
        // given
        Long lectureId = 1L;
        Long userId = 11L;

        // when
        when(lectureRepository.findByIdWithPessimisticLock(1L)).thenReturn(항해_플러스_특강);
        when(lectureRegistrationRepository.save(anyLong(), anyLong())).thenReturn(new LectureRegistration(
                1L,
                userId
        ));
        RegisterResponse response = lectureService.register(lectureId, userId);

        // then
        assertNotNull(response);
        assertEquals(response.lectureId(), 1L);
        assertEquals(response.name(), "항해 플러스 토요일 특강");
    }

    @Test
    @DisplayName("특강_신청_실패한_사용자는_실패했음을_리턴")
    void checkTest_특강_신청_실패한_사용자는_실패했음을_리턴() {
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
    @DisplayName("동일한_이름의_특강은_등록_불가")
    void addTest_동일한_이름의_특강은_등록_불가() {
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
    @DisplayName("특강_날짜가_지나지_않았고_신청자가_존재하면_삭제_불가")
    void deleteTest_특강_날짜가_지나지_않았고_신청자가_존재하면_삭제_불가() {
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