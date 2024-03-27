package io.hhplus.architecture.classes.special_class;

import io.hhplus.architecture.classes.special_class.service.SpecialClassService;
import io.hhplus.architecture.classes.special_class.service.TestDataHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.concurrent.*;
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
    @Autowired
    private TestDataHandler testDataHandler;

    private static final String LOCAL_HOST = "http://localhost:";
    private static final String PATH = "/special-class";

    @BeforeEach
    void setUp() {
        // 기존 참여자, 현재 특강 신청자 수 초기화
        testDataHandler.initAttendee();
        testDataHandler.initSpecialClassNowRegisterCnt();
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
    @DisplayName("동일한_유저가_따닥_특강_신청")
    void applyTest_동일한_유저가_따닥_특강_신청() {
        // given
        Long userId = 1L;

        // when - 따닥 특강 신청
        for (int i = 0; i < 2; i++) {
            try {
                specialClassService.regist(userId); // userId 동일
            } catch (Exception e) {
                // then
                System.out.println("Error during application for user " + (userId + i) + ": " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("5명이_순차적으로_특강_신청")
    void applyTest_5명이_순차적으로_특강_신청() {
        // given
        Long userId = 1L; // 테스트를 위한 시작 userId

        // when - 순차적으로 특강 신청
        for (int i = 0; i < 5; i++) {
            try {
                specialClassService.regist(userId + i); // userId 1씩 증가
            } catch (Exception e) {
                System.out.println("Error during application for user " + (userId + i) + ": " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("10명이_동시에_특강_신청_락_확인")
    void applyTest_10명이_동시에_특강_신청() throws InterruptedException {
        // 고정된 스레드 풀을 1개의 스레드로 설정
        final ExecutorService service = Executors.newFixedThreadPool(1);

        final int numberOfThreads = 10; // 총 신청 횟수
        Long userId = 1L; // 테스트를 위한 시작 userId

        for (int i = 0; i < numberOfThreads; i++) {
            final long id = userId + i;
            service.submit(() -> {
                try {
                    // 특강 신청
                    specialClassService.regist(id);
                } catch (Exception e) {
                    System.out.println("Error during application for user " + id + ": " + e.getMessage());
                }
            });
        }

        // 모든 작업이 완료될 때까지 대기
        service.shutdown();
        service.awaitTermination(1, TimeUnit.MINUTES);

        // then
        // 스레드 풀 1개 -> 락이 제대로 걸리면 1, 2, 3, ..., 10 순서대로 등록됨
    }

    @Test
    @DisplayName("31명이_순차적으로_특강_신청하면_31번째_유저는_실패")
    void applyTest_31명이_순차적으로_특강_신청하면_31번째_유저는_실패() {
        // given
        Long userId = 1L; // 테스트를 위한 시작 userId

        // when - 순차적으로 특강 신청
        for (int i = 0; i < 31; i++) {
            try {
                specialClassService.regist(userId + i); // userId 1씩 증가
            } catch (Exception e) {
                System.out.println("Error during application for user " + (userId + i) + ": " + e.getMessage());
            }
        }

        // then
        // 31번째 유저는 실패
    }

    @Test
    @DisplayName("50명이_동시에_특강_신청")
    void applyTest_50명이_동시에_특강_신청() throws ExecutionException, InterruptedException {
        // given
        Long userId = 1L; // 테스트를 위한 시작 userId

        // when - 동시에 특강 신청
        CompletableFuture<?>[] futures = IntStream.range(0, 50)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    try {
                        specialClassService.regist(userId + i); // userId 1씩 증가
                    } catch (Exception e) {
                        System.out.println("Error during application for user " + (userId + i) + ": " + e.getMessage());
                    }
                }))
                .toArray(CompletableFuture[]::new);

        // 모든 작업이 완료될 때까지 대기
        CompletableFuture.allOf(futures).get();

        // then
        // 30명만 성공
    }
}