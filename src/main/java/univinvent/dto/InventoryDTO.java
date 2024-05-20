package univinvent.dto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryDTO {
    int itemId;

    String itemName;

    String description;

    int totalQuantity;

    int warehouseId;

    HashMap<String,Long> statusQuantity;

    String imagePath;
}
