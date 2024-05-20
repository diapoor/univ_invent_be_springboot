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
@Table(name = "borrowings")
public class Borrow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "borrowing_id")
    int borrowingId;

    @Column(name = "item_id")
     int itemId;

    @Column(name = "borrower")
     String borrowerName;

    @Column(name = "borrower_phone")
     String borrowerPhone;

    @Column(name = "borrowed_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date borrowedDate;

    @Column(name = "due_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date dueDate;

    @Column(name = "returned_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date returnedDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Status status;

    public enum Status {
        BORROWED,RETURNED,OVERDUE
    }
}
