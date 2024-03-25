package io.hhplus.architecture.special_class;

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

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class SpecialClassIntegrationTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate;
    private final SpecialClassService specialClassService;

    private static final String LOCAL_HOST = "http://localhost:";
    private static final String PATH = "/special-class";

    SpecialClassIntegrationTest(TestRestTemplate restTemplate, SpecialClassService specialClassService) {
        this.restTemplate = restTemplate;
        this.specialClassService = specialClassService;
    }

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("특강_신청_여부_조회")
    void checkTest_특강_신청_여부_조회() {
        // given
        Long userId = 1L;

        // when
        ResponseEntity<Boolean> response = restTemplate.getForEntity(LOCAL_HOST + port + PATH + "/" + userId, Boolean.class);

        // then
        assertThat(Objects.requireNonNull(response.getBody()).booleanValue()).isEqualTo(false);
    }

    @Test
    void apply() {

    }

    @Test
    void check() {
    }
}