package io.hhplus.architecture.classes.service;

import io.hhplus.architecture.classes.special_class.domain.entity.Attendee;

public interface ClassInterface {

    // 강의 신청
    Attendee apply(Long userId);

    // 강의 신청 여부 조회
    String check(Long userId);
}
