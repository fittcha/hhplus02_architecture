package io.hhplus.architecture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ArchitectureApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArchitectureApplication.class, args);
    }
}

