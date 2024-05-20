package univinvent.mapper;
import org.mapstruct.Mapper;
import univinvent.dto.InventoryDTO;
import univinvent.entities.Inventory;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    Inventory toInventory(InventoryDTO inventoryDTO);
    InventoryDTO toInventoryDTO(Inventory inventory);
}
