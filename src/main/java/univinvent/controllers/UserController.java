package univinvent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import univinvent.dto.UserDTO;
import univinvent.entities.User;
import univinvent.services.UserService;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<User>> getUserByUsernameOrFullName(@PathVariable("keyword")String keyword) {
        return ResponseEntity.ok(userService.getUserByUsernameOrFullName(keyword));
    }

    @GetMapping("/get/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable("username")String username) {
        try {
            return ResponseEntity.ok(userService.getUserByUsername(username));
        }catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody UserDTO userDTO) {
        try {
            userService.changePassword(userDTO);
            return ResponseEntity.ok("Changed password successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }

    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> delete(@PathVariable("username")String username) {
        userService.delete(username);
        return ResponseEntity.ok("Delete Successfully");
    }

}
