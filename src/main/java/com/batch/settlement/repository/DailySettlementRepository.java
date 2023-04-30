package com.batch.settlement.repository;

import com.batch.settlement.entity.DailySettlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailySettlementRepository extends JpaRepository<DailySettlement, Long> {
}
