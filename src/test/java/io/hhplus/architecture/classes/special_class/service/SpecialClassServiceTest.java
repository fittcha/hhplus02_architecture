package io.hhplus.architecture.classes.special_class.service;

import io.hhplus.architecture.classes.special_class.domain.entity.ClassRegistration;
import io.hhplus.architecture.classes.special_class.repository.ClassRegistrationRepository;
import io.hhplus.architecture.classes.special_class.repository.SpecialClassRepository;
import io.hhplus.architecture.classes.special_class.domain.SpecialClassCustomException;
import io.hhplus.architecture.classes.special_class.domain.entity.SpecialClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SpecialClassServiceTest {

    private SpecialClassService specialClassService;
    private SpecialClassRepository specialClassRepository;
    private ClassRegistrationRepository classRegistrationRepository;

    private SpecialClass 항해_플러스_특강;

    @BeforeEach
    void setUp() {
        // mocking
        specialClassRepository = Mockito.mock(SpecialClassRepository.class);
        classRegistrationRepository = Mockito.mock(ClassRegistrationRepository.class);
        specialClassService = new SpecialClassService(
                new SpecialClassRegister(specialClassRepository, classRegistrationRepository),
                new SpecialClassReader(specialClassRepository, classRegistrationRepository),
                new SpecialClassValidator()
        );

        // 항해 플러스 토요일 특강 세팅
        항해_플러스_특강 = SpecialClass.builder()
                .specialClassId(1L)
                .name("항해 플러스 토요일 특강")
                .nowRegisterCnt(0)
                .maxRegisterCnt(30)
                .classDatetime(LocalDateTime.of(2024, 4, 20, 13,0, 0))
                .build();
    }

    @Test
    @DisplayName("동일한_특강_중복_신청_불가")
    void applyTest_동일한_특강_중복_신청_불가() {
        // when
        when(specialClassRepository.findByIdWithPessimisticLock(1L)).thenReturn(항해_플러스_특강);
        when(classRegistrationRepository.existsBySpecialClassAndUserId(항해_플러스_특강, 1L)).thenReturn(true);

        // then
        SpecialClassCustomException expected = assertThrows(SpecialClassCustomException.class, () -> {
            specialClassService.regist(1L);
        });
        assertThat(expected.getMessage()).isEqualTo("이미 신청된 특강입니다.");
    }

    @Test
    @DisplayName("신청자가_30명_이상이면_신청_불가")
    void applyTest_신청자가_30명_이상이면_신청_불가() {
        // given
        Long userId = 11L;
        SpecialClass 항해_플러스_특강_FULL = SpecialClass.builder()
                .specialClassId(1L)
                .name("꽉 찬 항해 플러스 특강")
                .nowRegisterCnt(30)
                .maxRegisterCnt(30)
                .classDatetime(LocalDateTime.of(2024, 4, 20, 13,0, 0))
                .build();

        // when
        when(specialClassRepository.findByIdWithPessimisticLock(1L)).thenReturn(항해_플러스_특강_FULL);
        when(classRegistrationRepository.existsBySpecialClassAndUserId(항해_플러스_특강_FULL, 1L)).thenReturn(false);

        // then
        SpecialClassCustomException expected = assertThrows(SpecialClassCustomException.class, () -> {
            specialClassService.regist(userId);
        });
        assertThat(expected.getMessage()).isEqualTo("정원이 초과되어 수강 신청에 실패했습니다.");
    }

    @Test
    @DisplayName("신청_성공")
    void applyTest_신청_성공() throws ExecutionException, InterruptedException {
        // given
        Long userId = 11L;

        // when
        when(specialClassRepository.findByIdWithPessimisticLock(1L)).thenReturn(항해_플러스_특강);
        when(classRegistrationRepository.existsBySpecialClassAndUserId(항해_플러스_특강, 1L)).thenReturn(false);
        when(classRegistrationRepository.countUserIdBySpecialClass_SpecialClassId(1L)).thenReturn(10);
        when(classRegistrationRepository.save(any())).thenReturn(new ClassRegistration(
                항해_플러스_특강,
                userId
        ));
        ClassRegistration classRegistration = specialClassService.regist(userId);

        // then
        assertNotNull(classRegistration);
        assertEquals(classRegistration.getSpecialClass().getName(), "항해 플러스 토요일 특강");
        assertEquals(classRegistration.getUserId(), 11L);
    }

    @Test
    @DisplayName("특강_신청_성공한_사용자는_성공했음을_리턴")
    void checkTest_특강_신청_성공한_사용자는_성공했음을_리턴() {
        // given
        Long userId = 1L;

        // when
        when(specialClassRepository.findById(1L)).thenReturn(Optional.ofNullable(항해_플러스_특강));
        when(classRegistrationRepository.existsBySpecialClassAndUserId(항해_플러스_특강, userId)).thenReturn(true);
        String applicantYn = specialClassService.check(userId);

        // then
        assertThat(applicantYn.equals("신청 완료"));
    }

    @Test
    @DisplayName("특강_신청_실패한_사용자는_실패했음을_리턴")
    void checkTest_특강_신청_실패한_사용자는_실패했음을_리턴() {
        // given
        Long userId = 1L;

        // when
        when(specialClassRepository.findById(1L)).thenReturn(Optional.ofNullable(항해_플러스_특강));
        when(classRegistrationRepository.existsBySpecialClassAndUserId(항해_플러스_특강, userId)).thenReturn(false);
        String applicantYn = specialClassService.check(userId);

        // then
        assertThat(applicantYn.equals("신청 완료"));
    }
}