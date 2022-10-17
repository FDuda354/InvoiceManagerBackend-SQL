package net.dudios.invoicemanagerbackend.invoice;

import lombok.RequiredArgsConstructor;
import net.dudios.invoicemanagerbackend.user.AppUser;
import net.dudios.invoicemanagerbackend.user.Roles;
import net.dudios.invoicemanagerbackend.user.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class MongoInit {

    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void fillDB() {
        AppUser user1 = AppUser.builder()
                .username("filip")
                .password("filip1234")
                .email("filip@wp.pl")
                .invoices(new ArrayList<>())
                .role(Roles.ROLE_ADMIN)
                .build();

        AppUser user2 = AppUser.builder()
                .username("kamil")
                .password("kamil1234")
                .email("kamil@wp.pl")
                .invoices(new ArrayList<>())
                .role(Roles.ROLE_USER)
                .build();


        Invoice invoice1 = Invoice.builder().companyName("Dudios").companyAddress("ul. Kolejowa 1")
                .companyNIP("1234567890").invoiceNumber("1/2020").invoiceDate("2020-01-01")
                .invoiceDescription("IT services").priceNetto(BigDecimal.valueOf(250))
                .priceBrutto(BigDecimal.valueOf(300)).paid(false).build();

        Invoice invoice2 = Invoice.builder().companyName("Dudios").companyAddress("ul. Kolejowa 1")
                .companyNIP("1234567890").invoiceNumber("2/2020").invoiceDate("2020-01-01")
                .invoiceDescription("car services").priceNetto(BigDecimal.valueOf(2520))
                .priceBrutto(BigDecimal.valueOf(3100)).paid(false).build();

        user1.getInvoices().add(invoice1);
        user1.getInvoices().add(invoice2);

        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(user1.getEmail()));

        if (mongoTemplate.find(query, AppUser.class).isEmpty()) {
            userRepository.save(user1);
        }
        else {
            System.out.println("User already exists"+user1.getEmail());
        }

        Query query2 = new Query();
        query2.addCriteria(Criteria.where("email").is(user2.getEmail()));
        if (mongoTemplate.find(query2, AppUser.class).isEmpty()) {
            userRepository.save(user2);
        }
        else {
            System.out.println("User already exists"+user2.getEmail());
        }

    }
}
