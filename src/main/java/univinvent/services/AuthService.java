package univinvent.services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import univinvent.authenticaion.AuthenticationRequest;
import univinvent.authenticaion.AuthenticationResponse;
import univinvent.dto.IntrospectRequest;
import univinvent.dto.IntrospectResponse;
import univinvent.entities.User;
import univinvent.repositories.UserRepository;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@Log4j2
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws AuthenticationException {
        User user = userRepository.getUserByUsername(authenticationRequest.getUsername());
        if (user == null || !encoder.matches(authenticationRequest.getPassword(), user.getPassword()))
            throw new AuthenticationException("Incorrect Account");
        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .Token(token)
                .build();
    }

    public void register(User user) {
        if(userRepository.existsByUsername(user.getUsername())) throw new RuntimeException("Username already exists.");
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }
    public String resetPassword(String username, String fullName) {
        User user = userRepository.getUserByUsernameAndFullName(username, fullName);
        if (user == null) {
            throw new RuntimeException("Account information is incorrect. Please try again.");
        }
        String newPassword = user.getPassword().length() > 7 ? user.getPassword().substring(user.getPassword().length() - 7) : user.getPassword();
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
        return newPassword;
    }

    @Value("${jwt.SIGNER_KEY}")
    private String SIGNER_KEY;
    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer(user.getFullName())
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header,payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token",e);
            throw new RuntimeException(e);
        }
    }
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        String token = request.getToken();
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        return IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date())).build();
    }
}
