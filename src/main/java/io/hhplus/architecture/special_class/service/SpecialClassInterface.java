package io.hhplus.architecture.special_class.service;

import io.hhplus.architecture.special_class.domain.entity.Attendee;

public interface SpecialClassInterface {

    Attendee apply(Long userId);

    boolean check(Long userId);
}
