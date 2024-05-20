package univinvent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import univinvent.entities.LossesAndDamage;
import univinvent.entities.Maintenance;

import java.util.List;

@Repository
public interface LossesAndDamagesRepository extends JpaRepository<LossesAndDamage,Integer> {
    public LossesAndDamage getLossesAndDamageByReportId(int id);
    @Query("SELECT lad FROM LossesAndDamage lad JOIN Inventory i ON lad.itemId = i.itemId WHERE i.itemName like :keyword")
    public List<LossesAndDamage> getLossesAndDamageByKeyword(@Param("keyword")String keyword);
    @Query("SELECT lad FROM LossesAndDamage lad  WHERE lad.reportType = :type")
    public List<LossesAndDamage> getLossesAndDamageByReportType(@Param("type") LossesAndDamage.Type type);
}
