package io.hhplus.architecture.special_class.service;

import io.hhplus.architecture.special_class.domain.entity.Attendee;

import java.util.concurrent.CompletableFuture;

public interface SpecialClassInterface {

    // 특강 신청
    CompletableFuture<Attendee> apply(Long userId);

    // 특강 신청 여부 조회
    String check(Long userId);
}
