package net.dudios.invoicemanagerbackend.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import net.dudios.invoicemanagerbackend.invoice.Invoice;
import net.dudios.invoicemanagerbackend.security.CustomAuthorityDeserializer;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="USERS")
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;


    private String username;
    private String password;

    private String email;

   private String roles;


    @OneToMany(mappedBy = "appUser")
    private List<Invoice> invoices;

    public AppUser(Long l, String filip, String s, String s1, String name) {
        this.id = l;
        this.username = filip;
        this.password = s;
        this.email = s1;
        this.roles = name;
    }

    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(roles));    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

