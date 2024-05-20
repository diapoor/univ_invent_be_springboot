package univinvent.repositories;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import univinvent.entities.Borrow;
import univinvent.entities.Maintenance;

import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance,Integer> {
    public Maintenance getMaintenanceByMaintenanceId(int id);
    @Query("SELECT m FROM Maintenance m WHERE m.status = :status")
    public List<Maintenance> getMaintenanceByStatus(@Param("status")Maintenance.Status status);
    @Query("SELECT m FROM Maintenance m JOIN Inventory i ON m.itemId = i.itemId WHERE i.itemName like :keyword")
    public List<Maintenance> getMaintenanceByItemName(@Param("keyword")String keyword);
}
