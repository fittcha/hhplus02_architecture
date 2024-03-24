package io.hhplus.architecture.specialClass;

import io.hhplus.architecture.specialClass.service.SpecialClassService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestConstructor;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class SpecialClassIntegrationTest {
// TODO
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
    void apply() {
    }

    @Test
    void check() {
    }
}