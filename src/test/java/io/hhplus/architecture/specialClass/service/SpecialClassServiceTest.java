package io.hhplus.architecture.specialClass.service;

import io.hhplus.architecture.specialClass.domain.entity.Applicant;
import io.hhplus.architecture.specialClass.domain.entity.SpecialClass;
import io.hhplus.architecture.specialClass.repository.ApplicantRepository;
import io.hhplus.architecture.specialClass.repository.SpecialClassRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SpecialClassServiceTest {

    private SpecialClassService specialClassService;
    private SpecialClassRepository specialClassRepository;
    private ApplicantRepository applicantRepository;

    private SpecialClass 항해_플러스_특강;

    @BeforeEach
    void setUp() {
        // mocking
        specialClassRepository = Mockito.mock(SpecialClassRepository.class);
        applicantRepository = Mockito.mock(ApplicantRepository.class);
        specialClassService = new SpecialClassService(
                new SpecialClassManager(specialClassRepository, applicantRepository),
                new SpecialClassReader(specialClassRepository, applicantRepository),
                new SpecialClassValidator()
        );

        // 항해 플러스 토요일 특강 세팅
        항해_플러스_특강 = SpecialClass.builder()
                .specialClassId(1L)
                .name("항해 플러스 토요일 특강")
                .maxApplicantCnt(30)
                .classDatetime(LocalDateTime.of(2024, 4, 20, 13,0, 0))
                .build();
        when(specialClassRepository.findById(1L)).thenReturn(Optional.ofNullable(항해_플러스_특강));
    }

    @Test
    @DisplayName("동일한_특강_중복_신청_불가")
    void applyTest_동일한_특강_중복_신청_불가() {
        // when
        when(specialClassRepository.existsById(1L)).thenReturn(true);
        when(applicantRepository.existsBySpecialClassAndUserId(항해_플러스_특강, 1L)).thenReturn(true);

        // then
        RuntimeException expected = assertThrows(RuntimeException.class, () -> {
            specialClassService.apply(1L);
        });
        assertThat(expected.getMessage()).isEqualTo("이미 신청된 특강입니다.");
    }

    @Test
    @DisplayName("신청자가_30명_이상이면_신청_불가")
    void applyTest_신청자가_30명_이상이면_신청_불가() {
        // given
        Long userId = 11L;

        // when
        when(specialClassRepository.existsById(1L)).thenReturn(true);
        when(applicantRepository.existsBySpecialClassAndUserId(항해_플러스_특강, 1L)).thenReturn(false);
        when(applicantRepository.countUserIdBySpecialClass(항해_플러스_특강)).thenReturn(30);

        // then
        RuntimeException expected = assertThrows(RuntimeException.class, () -> {
            specialClassService.apply(userId);
        });
        assertThat(expected.getMessage()).isEqualTo("정원이 초과되어 수강 신청에 실패했습니다.");
    }

    @Test
    @DisplayName("신청_성공")
    void applyTest_신청_성공() {
        // given
        Long userId = 11L;

        // when
        when(specialClassRepository.existsById(1L)).thenReturn(true);
        when(applicantRepository.existsBySpecialClassAndUserId(항해_플러스_특강, 1L)).thenReturn(false);
        when(applicantRepository.countUserIdBySpecialClass(항해_플러스_특강)).thenReturn(10);
        when(applicantRepository.save(any())).thenReturn(new Applicant(
                항해_플러스_특강,
                userId
        ));
        Applicant applicant = specialClassService.apply(userId);

        // then
        assertNotNull(applicant);
        assertEquals(applicant.getSpecialClass().getName(), "항해 플러스 토요일 특강");
        assertEquals(applicant.getUserId(), 11L);
    }

    @Test
    @DisplayName("특강_신청_성공한_사용자는_성공했음을_리턴")
    void checkTest_특강_신청_성공한_사용자는_성공했음을_리턴() {
        // given
        Long userId = 1L;

        // when
        when(applicantRepository.existsBySpecialClassAndUserId(항해_플러스_특강, userId)).thenReturn(true);
        boolean applicantYn = specialClassService.check(userId);

        // then
        assertTrue(applicantYn);
    }

    @Test
    @DisplayName("특강_신청_실패한_사용자는_실패했음을_리턴")
    void checkTest_특강_신청_실패한_사용자는_실패했음을_리턴() {
        // given
        Long userId = 1L;

        // when
        when(applicantRepository.existsBySpecialClassAndUserId(항해_플러스_특강, userId)).thenReturn(false);
        boolean applicantYn = specialClassService.check(userId);

        // then
        assertFalse(applicantYn);
    }
}