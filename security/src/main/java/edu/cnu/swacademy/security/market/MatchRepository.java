package edu.cnu.swacademy.security.market;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {

  List<Match> findByStockId(Long stockId);

  List<Match> findByMakerOrderId(Long makerOrderId);

  List<Match> findByTakerOrderId(Long takerOrderId);

  Optional<Match> findFirstByStockIdAndCreatedAtBetweenOrderByCreatedAtAsc(int id, LocalDateTime localDateTime, LocalDateTime localDateTime1);
}
