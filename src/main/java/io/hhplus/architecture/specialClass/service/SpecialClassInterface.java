package io.hhplus.architecture.specialClass.service;

import io.hhplus.architecture.specialClass.domain.entity.Applicant;
import io.hhplus.architecture.specialClass.domain.entity.SpecialClass;

public interface SpecialClassInterface {

    Applicant apply(Long userId);

    boolean check(Long userId);
}
