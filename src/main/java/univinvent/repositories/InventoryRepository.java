package univinvent.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import univinvent.entities.Borrow;
import univinvent.entities.Inventory;
import univinvent.entities.LossesAndDamage;
import univinvent.entities.Maintenance;

import java.util.List;

@Repository
@Transactional
public interface InventoryRepository extends JpaRepository<Inventory,Integer> {
    public Inventory getInventoriesByItemId(int id);
    public Inventory getInventoriesByItemNameAndDescriptionAndWarehouseId(String itemName,String description,int id);
    public boolean existsInventoriesByItemNameAndDescription(String itemName,String description);
    @Query("SELECT COUNT(*) FROM Borrow b WHERE b.itemId = :itemId AND b.status = :status")
    public Long countItemInBorrow(@Param("itemId") int itemId, @Param("status")Borrow.Status status);
    @Query("SELECT COUNT(*) FROM Maintenance m WHERE m.itemId = :itemId AND m.status = :status")
    public Long countItemInMaintenance(@Param("itemId") int itemId, @Param("status") Maintenance.Status status);
    @Query("SELECT COUNT(*) FROM LossesAndDamage lad WHERE lad.itemId = :itemId AND lad.reportType = :type")
    public Long countItemInLossesAndDamage(@Param("itemId") int itemId, @Param("type") LossesAndDamage.Type type);
    @Query("SELECT COUNT(*) FROM Borrow b WHERE b.itemId = :itemId AND b.status != :status")
    public Long countItemInBorrowNot(@Param("itemId") int itemId, @Param("status")Borrow.Status status);
    @Query("SELECT COUNT(*) FROM Maintenance m WHERE m.itemId = :itemId AND m.status != :status")
    public Long countItemInMaintenanceNot(@Param("itemId") int itemId, @Param("status") Maintenance.Status status);
    @Query("SELECT COUNT(*) FROM Borrow b WHERE b.itemId = :itemId")
    public Long countSumItemInLossesAndDamaged(@Param("itemId") int itemId);

    @Query("SELECT i FROM Inventory i WHERE i.itemName like :keyword OR i.description like :keyword")
    public List<Inventory> getInventoriesByItemNameOrDescription(@Param("keyword")String keyword);

    public boolean existsInventoriesByItemId(int id);

    @Query("SELECT EXISTS (SELECT w  from  WareHouse w where w.warehouseId = :id)")
    boolean checkWarehouse(@Param("id")int id);


}
