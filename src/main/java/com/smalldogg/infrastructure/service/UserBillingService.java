package com.smalldogg.infrastructure.service;

import com.smalldogg.application.stream.domain.billing.UserBillingAggregate;
import com.smalldogg.infrastructure.entity.UserBilling;
import com.smalldogg.infrastructure.repository.UserBillingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserBillingService {

    private final UserBillingRepository userBillingRepository;

    @Transactional
    public void save(UserBillingAggregate aggregate, long windowStart, long windowEnd) {
        UserBilling userBilling = UserBilling.of(aggregate);
        userBillingRepository.save(userBilling);
    }
}
