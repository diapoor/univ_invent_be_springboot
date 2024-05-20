package univinvent.controllers;


import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import univinvent.authenticaion.AuthenticationRequest;
import univinvent.dto.IntrospectRequest;
import univinvent.entities.User;
import univinvent.services.AuthService;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<?> login( @RequestBody @Valid AuthenticationRequest authenticationRequest) {
        try {
            return ResponseEntity.ok(authService.authenticate(authenticationRequest));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid User user) {
            authService.register(user);
            return ResponseEntity.ok("Register Successfully.");
    }
    @PostMapping("/recoveryPassword")
    public ResponseEntity<String> recoveryPassword(@RequestBody @Valid User user) {
            String newPassword = authService.resetPassword(user.getUsername(), user.getFullName());
            return ResponseEntity.ok(newPassword);
    }
    @PostMapping("/introspect")
    public ResponseEntity<?> introspect(@RequestBody IntrospectRequest introspectRequest) {
        try {
            return ResponseEntity.ok(authService.introspect(introspectRequest));
        } catch (JOSEException | ParseException e) {
            return ResponseEntity.status(403).body("Token not valid");
        }
    }
}
