package io.hhplus.architecture.controller.dto;

import jakarta.annotation.Nonnull;

public record RegisterRequest(
        @Nonnull Long userId
) {
}
