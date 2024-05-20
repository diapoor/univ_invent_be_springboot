package univinvent.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    int userId;

    @Column(name = "username")
    @Size(min = 4,max = 15,message = "Username must be at least 4-15 character")
    String username;

    @Column(name = "hash_password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 4,max =500,message = "Password must be at least 4-15 character")
    String password;


    @Column(name = "full_name")
    String fullName;


}
