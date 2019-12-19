package pl.edu.wat.smart.home.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.edu.wat.smart.home.entity.WeatherStatus;

import java.util.List;

public interface WeatherStatusRepo extends JpaRepository<WeatherStatus, Long> {

    @Query("SELECT ws FROM WeatherStatus ws")
    List<WeatherStatus> getLast(Pageable pageable);
}
