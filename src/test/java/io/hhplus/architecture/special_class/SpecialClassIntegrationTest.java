package io.hhplus.architecture.special_class;

import io.hhplus.architecture.special_class.domain.entity.Attendee;
import io.hhplus.architecture.special_class.domain.entity.SpecialClass;
import io.hhplus.architecture.special_class.repository.AttendeeRepository;
import io.hhplus.architecture.special_class.repository.SpecialClassRepository;
import io.hhplus.architecture.special_class.service.SpecialClassService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpecialClassIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private SpecialClassService specialClassService;

    private static final String LOCAL_HOST = "http://localhost:";
    private static final String PATH = "/special-class";

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("특강_신청_여부_조회")
    void checkTest_특강_신청_여부_조회() {
        // given
        Long userId = 1L;

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
    }
}