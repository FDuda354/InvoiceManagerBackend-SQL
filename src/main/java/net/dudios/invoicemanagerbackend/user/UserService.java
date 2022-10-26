package net.dudios.invoicemanagerbackend.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dudios.invoicemanagerbackend.security.AuthRequest;
import org.springframework.beans.factory.annotation.Value;
import net.dudios.invoicemanagerbackend.security.AuthResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    @Value("${secret.key}")
    private String SECRET_KEY;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AppUser addUser(AppUser user) {
        return userRepository.save(userBuilder(user));
    }

    public AppUser getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("invoice not found"));
    }


    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long userId) {
        if(userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new RuntimeException("user not found");
        }
    }

    private AppUser userBuilder(AppUser appUser) {
        appUser.setRoles("ROLE_"+Role.USER.name());
        appUser.setInvoices(new ArrayList<>());
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return appUser;
    }

    public AuthResponse getJwt(AuthRequest authRequest){
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));
            AppUser appUser = (AppUser) authentication.getPrincipal();

            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
            String token = JWT.create()
                    .withIssuer("Admin")
                    .withSubject(appUser.getUsername())
                    .withClaim("roles", appUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .sign(algorithm);

            return new AuthResponse(token, appUser.getUsername(), appUser.getId().toString());

        }catch (Exception e){
            return null;
        }
    }
}

