package net.dudios.invoicemanagerbackend.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.dudios.invoicemanagerbackend.invoice.Invoice;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="USERS")
public class AppUser {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;


    private String username;
    private String password;

    private String email;

   @Enumerated(EnumType.STRING)
   private Roles roles;


    @OneToMany(mappedBy = "appUser")
    private Set<Invoice> invoices;


}

