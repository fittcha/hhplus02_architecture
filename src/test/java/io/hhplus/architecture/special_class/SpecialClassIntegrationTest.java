package io.hhplus.architecture.special_class;

import io.hhplus.architecture.special_class.domain.entity.Attendee;
import io.hhplus.architecture.special_class.domain.entity.SpecialClass;
import io.hhplus.architecture.special_class.repository.AttendeeRepository;
import io.hhplus.architecture.special_class.repository.SpecialClassRepository;
import io.hhplus.architecture.special_class.service.SpecialClassService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest()
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class SpecialClassIntegrationTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate;
    private final SpecialClassService specialClassService;
    private final SpecialClassRepository specialClassRepository;
    private final AttendeeRepository attendeeRepository;

    private static final String LOCAL_HOST = "http://localhost:";
    private static final String PATH = "/special-class";

    SpecialClassIntegrationTest(TestRestTemplate restTemplate, SpecialClassService specialClassService, SpecialClassRepository specialClassRepository, AttendeeRepository attendeeRepository) {
        this.restTemplate = restTemplate;
        this.specialClassService = specialClassService;
        this.specialClassRepository = specialClassRepository;
        this.attendeeRepository = attendeeRepository;
    }

    @BeforeEach
    void setUp() {
//        specialClassRepository.save(SpecialClass.builder()
//                .specialClassId(1L)
//                .name("항해 플러스 토요일 특강")
//                .nowRegisterCnt(0)
//                .maxRegisterCnt(30)
//                .classDatetime(LocalDateTime.of(2024, 4, 20, 13,0, 0))
//                .build());
    }

    @Test
    @DisplayName("특강_신청_여부_조회")
    void checkTest_특강_신청_여부_조회() {
        // given
        Long userId = 1L;
        attendeeRepository.save(new Attendee(SpecialClass.builder()
                .specialClassId(1L)
                .name("항해 플러스 토요일 특강")
                .nowRegisterCnt(0)
                .maxRegisterCnt(30)
                .classDatetime(LocalDateTime.of(2024, 4, 20, 13,0, 0))
                .build(), 1L));

        // when
        ResponseEntity<String> response = restTemplate.getForEntity(LOCAL_HOST + port + PATH + "/" + userId, String.class);

        // then
        assertThat(Objects.requireNonNull(response.getBody()).toString()).isEqualTo("신청 완료");
    }

    @Test
    @Transactional
    @DisplayName("5명이_동시에_특강_신청")
    void applyTest_5명이_동시에_특강_신청() throws ExecutionException, InterruptedException {
        // given
        Long userId = 1L; // 테스트를 위한 userId 기준점

        // when
        // 동시에 특강 신청
        CompletableFuture<?>[] futures = IntStream.range(0, 5)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    try {
                        specialClassService.apply(userId + i); // 각 사용자별로 다른 userId를 부여
                    } catch (Exception e) {
                        System.out.println("Error during application for user " + (userId + i) + ": " + e.getMessage());
                    }
                }))
                .toArray(CompletableFuture[]::new);

        // 모든 비동기 작업이 완료될 때까지 대기
        CompletableFuture.allOf(futures).get();

        // then
        int attendeeCnt = attendeeRepository.countUserIdBySpecialClass_SpecialClassId(1L);
        assertThat(attendeeCnt).isEqualTo(5);
    }
}