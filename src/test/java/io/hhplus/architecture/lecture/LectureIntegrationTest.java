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
    private static final String PATH = "/lecture";

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
    @DisplayName("특강_신청_여부_조회")
    void checkTest_특강_신청_여부_조회() {
        // given
        Long lectureId = lectureService.readAll().get(0).lectureId();

        // when
        ResponseEntity<String> response = restTemplate.getForEntity(LOCAL_HOST + port + PATH + "/" + lectureId + "?userId=1", String.class);

        // then
        assertThat(Objects.requireNonNull(response.getBody()).toString()).isEqualTo("신청 내역이 없습니다.");
    }

    @Test
    @DisplayName("동일한_유저가_따닥_특강_신청")
    void registerTest_동일한_유저가_따닥_특강_신청() {
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
    @DisplayName("5명이_순차적으로_특강_신청")
    void applyTest_5명이_순차적으로_특강_신청() {
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
    @DisplayName("스레드_1개_동시에_특강_신청_락_확인")
    void applyTest_스레드_1개_동시에_특강_신청_락_확인() throws InterruptedException {
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
    @DisplayName("31명이_순차적으로_특강_신청하면_31번째_유저는_실패")
    void applyTest_31명이_순차적으로_특강_신청하면_31번째_유저는_실패() {
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
    @DisplayName("50명이_동시에_특강_신청")
    void applyTest_50명이_동시에_특강_신청() throws ExecutionException, InterruptedException {
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
    @DisplayName("특강_신청_취소")
    void cancelTest_특강_신청_취소() {
        // given
        Long lectureId = lectureService.readAll().get(0).lectureId();
        Long userId = 1L;
        applyTest_5명이_순차적으로_특강_신청();

        // when
        restTemplate.postForEntity(LOCAL_HOST + port + PATH + "/" + lectureId + "/cancel", new UserIdRequest(userId), Void.class);
    }

    @Test
    @DisplayName("강의_등록")
    void addTest_강의_등록() {
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
    @DisplayName("강의_삭제")
    void deleteTest_강의_삭제() {
        // given
        Long lectureId = lectureService.readAll().get(0).lectureId();

        // when
        restTemplate.delete(LOCAL_HOST + port + PATH + "/" + lectureId);
    }
}