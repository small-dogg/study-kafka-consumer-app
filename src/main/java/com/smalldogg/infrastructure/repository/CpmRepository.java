package com.smalldogg.infrastructure.repository;

import com.smalldogg.application.stream.domain.cpm.CpmAggregate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CpmRepository extends JpaRepository<CpmAggregate, Long> {
}
