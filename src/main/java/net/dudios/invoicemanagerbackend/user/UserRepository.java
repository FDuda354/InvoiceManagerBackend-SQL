package net.dudios.invoicemanagerbackend.user;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<AppUser, String> {
        AppUser findByUsername(String username);
        AppUser findByEmail(String email);
}
