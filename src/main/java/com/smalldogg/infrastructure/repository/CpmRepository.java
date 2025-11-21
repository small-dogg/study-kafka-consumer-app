package com.smalldogg.infrastructure.repository;

import com.smalldogg.infrastructure.entity.UserCpmBilling;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CpmRepository extends JpaRepository<UserCpmBilling, Long> {
}
