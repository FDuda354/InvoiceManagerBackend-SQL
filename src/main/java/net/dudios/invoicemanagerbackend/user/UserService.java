package net.dudios.invoicemanagerbackend.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public AppUser addUser(AppUser user) {
        return userRepository.save(user);
    }

    public AppUser getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("invoice not found"));
    }


    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }
}

