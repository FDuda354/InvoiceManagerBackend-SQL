package net.dudios.invoicemanagerbackend.user;

import lombok.AllArgsConstructor;
import net.dudios.invoicemanagerbackend.security.AuthRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/register")
    public ResponseEntity<AppUser> addUser(@RequestBody AppUser appUser){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/users").toUriString());
        return ResponseEntity.created(uri).body(this.userService.addUser(appUser));
    }

    @PostMapping("/login")
    public ResponseEntity<?> getJwt(@RequestBody AuthRequest authRequest){
        try{
            return ResponseEntity.ok().body(userService.getJwt(authRequest));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<AppUser> getUser(@RequestParam Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestParam Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }


}
