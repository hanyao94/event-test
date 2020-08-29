package com.seven.event.infrastructure.tx;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Transactional(value = "tms-service.txManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public @interface TMSTx {
}