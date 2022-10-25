package net.dudios.invoicemanagerbackend;

import lombok.AllArgsConstructor;
import net.dudios.invoicemanagerbackend.invoice.Invoice;
import net.dudios.invoicemanagerbackend.invoice.InvoiceService;
import net.dudios.invoicemanagerbackend.invoice.Payload;
import net.dudios.invoicemanagerbackend.user.AppUser;
import net.dudios.invoicemanagerbackend.user.UserService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;

@Component
@AllArgsConstructor
public class DBInit {

    private final UserService userService;
    private final InvoiceService invoiceService;


    @EventListener(ApplicationReadyEvent.class)
    public void fillDB() {
        AppUser user1 = AppUser.builder()
                .username("filip")
                .password("filip1234")
                .email("filip@wp.pl")
                .invoices(new HashSet<>())
                .roles("ROLE_ADMIN")
                .build();

        AppUser user2 = AppUser.builder()
                .username("kamil")
                .password("kamil1234")
                .email("kamil@wp.pl")
                .invoices(new HashSet<>())
                .roles("ROLE_USER")
                .build();


        Invoice invoice1 = Invoice.builder().companyName("Dudios").companyAddress("ul. Kolejowa 1")
                .companyNIP("1234567890").invoiceNumber("1/2020").invoiceDate("2020-01-01")
                .invoiceDescription("IT services").priceNetto(BigDecimal.valueOf(250))
                .priceBrutto(BigDecimal.valueOf(300)).paid(false).build();

        Invoice invoice2 = Invoice.builder().companyName("Dudios").companyAddress("ul. Kolejowa 1")
                .companyNIP("1234567890").invoiceNumber("2/2020").invoiceDate("2020-01-01")
                .invoiceDescription("car services").priceNetto(BigDecimal.valueOf(2520))
                .priceBrutto(BigDecimal.valueOf(3100)).paid(false).build();

        userService.addUser(user1);
        userService.addUser(user2);

        Payload payload1 = new Payload(invoice1,user1.getId());
        Payload payload2 = new Payload(invoice2,user1.getId());

        invoiceService.addInvoice(payload1);
        invoiceService.addInvoice(payload2);
    }
}
