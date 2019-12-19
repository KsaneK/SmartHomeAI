package pl.edu.wat.smart.home.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.wat.smart.home.entity.StatusHistory;

import java.util.List;

@Repository
public interface StatusHistoryRepo extends JpaRepository<StatusHistory, Long> {
    @Query("SELECT sh FROM StatusHistory sh WHERE sh.deviceCapability.id = :devCapId ORDER BY date DESC")
    List<StatusHistory> findLastByDeviceCapabilityId(@Param("devCapId") Long devCapId, Pageable pageable);
}
