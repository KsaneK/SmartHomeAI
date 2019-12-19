package pl.edu.wat.smart.home.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.wat.smart.home.entity.AirQualityStatus;

import java.util.List;

@Repository
public interface AirQualityStatusRepo extends JpaRepository<AirQualityStatus, Long> {
    @Query("SELECT aqs FROM AirQualityStatus aqs")
    List<AirQualityStatus> getLast(Pageable pageable);
}
