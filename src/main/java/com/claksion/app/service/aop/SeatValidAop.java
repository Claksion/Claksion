package com.claksion.app.service.aop;

import com.claksion.app.data.dto.request.SelectSeatRequest;
import com.claksion.app.service.SeatSelectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.oxm.ValidationFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class SeatValidAop {

    private final SeatSelectService seatSelectService;

    @Transactional(rollbackFor = ValidationFailureException.class)
    @Around("@annotation(AroundValidSeatOnRedis) && args(selectSeatRequest,..)")
    public Object validSeatOwnerOnRedis(ProceedingJoinPoint joinPoint, SelectSeatRequest selectSeatRequest) throws Throwable{
        // redis 검증
        seatSelectService.checkSeatOwnerOnRedis(selectSeatRequest);

        // sql 데이터 추가
        Object object = joinPoint.proceed();

        // redis 검증
        seatSelectService.checkSeatOwnerOnRedis(selectSeatRequest);
        return object;
    }
}

