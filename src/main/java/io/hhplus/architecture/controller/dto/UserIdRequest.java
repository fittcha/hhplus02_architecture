package io.hhplus.architecture.controller.dto;

import jakarta.validation.constraints.NotNull;

public record UserIdRequest(
        @NotNull Long userId
) {
}
