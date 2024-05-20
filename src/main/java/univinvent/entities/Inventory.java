package univinvent.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    int itemId;

    @Column(name = "item_name")
    String itemName;

    @Column(name = "description")
    String description;

    @Column(name = "total_quantity")
    int totalQuantity;

    @Column(name = "warehouse_id")
    int warehouseId;

    @Column(name = "image")
    String imagePath;
}
