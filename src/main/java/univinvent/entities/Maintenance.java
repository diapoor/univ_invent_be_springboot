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
@Table(name = "maintenance")
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maintenance_id")
    int maintenanceId;

    @Column(name = "item_id")
    int itemId;

    @Column(name = "issue_description")
    String issueDescription;

    @Column(name = "reported_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date reportedDate;

    @Column(name = "resolved_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date resolvedDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Status status;

    public  enum Status{
        PENDING,IN_PROGRESS,RESOLVED
    }
}
