package univinvent.authenticaion;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AuthenticationRequest {

    @Size(min = 4,max = 15,message = "Username must be 4-15 character")
    String username;
    @Size(min = 4,max = 15,message = "Password must be 4-15 character")
    String password;
}
