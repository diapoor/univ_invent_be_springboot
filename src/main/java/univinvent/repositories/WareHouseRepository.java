package univinvent.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import univinvent.entities.Inventory;
import univinvent.entities.User;
import univinvent.entities.WareHouse;

import java.util.List;
import java.util.Set;

@Repository
public interface WareHouseRepository extends JpaRepository<WareHouse,Integer> {
    @Query("SELECT w FROM WareHouse w WHERE w.warehouseId = :id")
    public WareHouse getWareHouseByWarehouseId(@Param("id") int id);

    public List<WareHouse> getWareHouseByName(String name);

    public boolean existsWareHouseByNameAndLocation(String name,String location);

    @Query("SELECT i FROM Inventory i WHERE i.warehouseId = :warehouseId")
    public Set<Inventory> getInventoryByWareHouseId(@Param("warehouseId")int warehouseId);

    @Query("SELECT sum(i.totalQuantity) FROM Inventory i WHERE i.warehouseId = :warehouseId")
    public Long getSumInventoryInWareHouse(@Param("warehouseId")int warehouseId);


    public boolean existsWareHouseByWarehouseId(int id);
}
