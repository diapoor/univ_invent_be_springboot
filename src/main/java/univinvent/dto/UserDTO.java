package univinvent.dto;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    String username;
    String fullName;
    String password;
    String newPassword;
    String confirmPassword;

}
