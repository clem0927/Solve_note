package kr.solvenote.usage.repository;

import kr.solvenote.usage.entity.DailyUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyUsageRepository extends JpaRepository<DailyUsage, Long> {

    Optional<DailyUsage> findByEmailAndUsageDate(String email, LocalDate date);

}