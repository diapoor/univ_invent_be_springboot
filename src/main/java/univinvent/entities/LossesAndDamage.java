package univinvent.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "lossesanddamages")
public class LossesAndDamage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    int reportId;

    @Column(name = "item_id")
    int itemId;

    @Column(name = "report_type")
    @Enumerated(EnumType.STRING)
    Type reportType;

    @Column(name = "description")
    String description;

    @Column(name = "resolved_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date resolvedDate;

    public enum Type{
        LOST,DAMAGED
    }
}
