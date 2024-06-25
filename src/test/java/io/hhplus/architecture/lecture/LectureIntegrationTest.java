package io.hhplus.architecture.lecture;

import io.hhplus.architecture.controller.dto.AddLectureRequest;
import io.hhplus.architecture.controller.dto.RegisterResponse;
import io.hhplus.architecture.controller.dto.UserIdRequest;
import io.hhplus.architecture.domain.lecture.service.LectureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LectureIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private LectureService lectureService;
    @Autowired
    private TestDataHandler testDataHandler;

    private static final String LOCAL_HOST = "http://localhost:";
    private static final String PATH = "/lectures";

    @BeforeEach
    void setUp() {
        // 특강 정보 초기화
        testDataHandler.initLecture();
        // 기존 참여자, 현재 특강 신청자 수 초기화
        testDataHandler.initRegistration();
        // 특강 정보 등록
        testDataHandler.addLecture();
    }

    @Test
    @DisplayName("특강 신청 여부 조회")
    void checkApplication() {
        // given
        Long lectureId = lectureService.readAll().get(0).lectureId();

        // when
        ResponseEntity<String> response = restTemplate.getForEntity(LOCAL_HOST + port + PATH + "/" + lectureId + "/application?userId=1", String.class);

        // then
        assertThat(Objects.requireNonNull(response.getBody()).toString()).isEqualTo("신청 내역이 없습니다.");
    }

    @Test
    @DisplayName("동일한 유저가 따닥 특강 신청")
    void concurrencyTestAtOneUserApply() {
        // given
        Long lectureId = lectureService.readAll().get(0).lectureId();
        Long userId = 1L;

        // when - 따닥 특강 신청
        for (int i = 0; i < 2; i++) {
            try {
                restTemplate.postForEntity(LOCAL_HOST + port + PATH + "/" + lectureId, new UserIdRequest(userId), RegisterResponse.class);
            } catch (Exception e) {
                // then
                System.out.println("Error during application for user " + (userId + i) + ": " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("5명이 순차적으로 특강 신청")
    void concurrencyTestAtFiveUserApply() {
        // given
        Long lectureId = lectureService.readAll().get(0).lectureId();
        Long userId = 1L; // 테스트를 위한 시작 userId

        // when - 순차적으로 특강 신청
        for (int i = 0; i < 5; i++) {
            try {
                restTemplate.postForEntity(LOCAL_HOST + port + PATH + "/" + lectureId, new UserIdRequest(userId + i), RegisterResponse.class);
            } catch (Exception e) {
                System.out.println("Error during application for user " + (userId + i) + ": " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("스레드 1개 동시에 특강 신청 락 확인")
    void concurrencyLockTestAtOneThread() throws InterruptedException {
        // 고정된 스레드 풀을 1개의 스레드로 설정
        final ExecutorService service = Executors.newFixedThreadPool(1);

        final int numberOfThreads = 10; // 총 신청 횟수
        Long lectureId = lectureService.readAll().get(0).lectureId();
        Long userId = 1L; // 테스트를 위한 시작 userId

        for (int i = 0; i < numberOfThreads; i++) {
            final long id = userId + i;
            service.submit(() -> {
                try {
                    // 특강 신청
                    restTemplate.postForEntity(LOCAL_HOST + port + PATH + "/" + lectureId, new UserIdRequest(id), RegisterResponse.class);
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
    @DisplayName("31명이 순차적으로 특강 신청하면 31번째 유저는 실패")
    void failAt31stUser() {
        // given
        Long lectureId = lectureService.readAll().get(0).lectureId();
        Long userId = 1L; // 테스트를 위한 시작 userId

        // when - 순차적으로 특강 신청
        for (int i = 0; i < 31; i++) {
            try {
                restTemplate.postForEntity(LOCAL_HOST + port + PATH + "/" + lectureId, new UserIdRequest(userId + i), RegisterResponse.class);
            } catch (Exception e) {
                System.out.println("Error during application for user " + (userId + i) + ": " + e.getMessage());
            }
        }

        // then
        // 31번째 유저는 실패
    }

    @Test
    @DisplayName("50명이 동시에 특강 신청")
    void concurrencyTestAt50UserApply() throws ExecutionException, InterruptedException {
        // given
        Long lectureId = lectureService.readAll().get(0).lectureId();
        Long userId = 1L; // 테스트를 위한 시작 userId

        // when - 동시에 특강 신청
        CompletableFuture<?>[] futures = IntStream.range(0, 50)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    try {
                        restTemplate.postForEntity(LOCAL_HOST + port + PATH + "/" + lectureId, new UserIdRequest(userId + i), RegisterResponse.class);
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

    @Test
    @DisplayName("특강 신청 취소")
    void cancelTest() {
        // given
        Long lectureId = lectureService.readAll().get(0).lectureId();
        Long userId = 1L;
        concurrencyTestAtFiveUserApply();

        // when
        restTemplate.postForEntity(LOCAL_HOST + port + PATH + "/" + lectureId + "/cancel", new UserIdRequest(userId), Void.class);
    }

    @Test
    @DisplayName("강의 등록")
    void addLectureTest() {
        // given
        AddLectureRequest request = AddLectureRequest.builder()
                .name("새로운 강의")
                .maxRegisterCnt(30)
                .lectureDatetime(ZonedDateTime.of(LocalDateTime.of(2024,5,10,14,0,0), ZoneId.of("Asia/Seoul")))
                .build();

        // when
        restTemplate.postForEntity(LOCAL_HOST + port + PATH, request, Object.class);
    }

    @Test
    @DisplayName("강의 삭제")
    void deleteTest() {
        // given
        Long lectureId = lectureService.readAll().get(0).lectureId();

        // when
        restTemplate.delete(LOCAL_HOST + port + PATH + "/" + lectureId);
    }
}