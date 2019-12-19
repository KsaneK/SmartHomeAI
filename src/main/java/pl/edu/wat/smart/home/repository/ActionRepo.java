package pl.edu.wat.smart.home.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.wat.smart.home.entity.Action;

import java.util.List;

@Repository
public interface ActionRepo extends JpaRepository<Action, Long> {

    @Query("SELECT a FROM Action a WHERE a.conditionCapability.id = :id")
    List<Action> findAllByConditionCapabilityId(@Param("id") Long id);

    @Query("SELECT a FROM Action a WHERE a.conditionCapability.device.owner.id = :id")
    List<Action> findAllByOwnerId(@Param("id") Long id);

    @Transactional
    void deleteAllByActionCapability_Device_Id(Long id);
    @Transactional
    void deleteAllByConditionCapability_Device_Id(Long id);
}
