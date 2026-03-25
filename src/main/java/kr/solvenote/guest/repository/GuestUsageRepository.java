package kr.solvenote.guest.repository;

import kr.solvenote.guest.entity.GuestUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface GuestUsageRepository extends JpaRepository<GuestUsage, Long> {

    Optional<GuestUsage> findByIpAndGuestDate(String ip, LocalDate date);

}