package com.smalldogg.infrastructure.repository;

import com.smalldogg.infrastructure.entity.UserBilling;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBillingRepository extends JpaRepository<UserBilling, Long> {
}
