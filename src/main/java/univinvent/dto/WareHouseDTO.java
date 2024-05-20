package univinvent.dto;

import lombok.Builder;
import lombok.Data;
import univinvent.entities.Inventory;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder

public class WareHouseDTO {
    private int warehouseId;
    private String name;
    private String location;
    Set<Inventory> inventories = new HashSet<>();
}
