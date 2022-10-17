package net.dudios.invoicemanagerbackend.invoice;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class MongoInit {

    private final InvoiceRepository invoiceRepository;
    private final MongoTemplate mongoTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void fillDB() {
        Invoice invoice = Invoice.builder().companyName("Dudios").companyAddress("ul. Kolejowa 1")
                .companyNIP("1234567890").invoiceNumber("1/2020").invoiceDate("2020-01-01")
                .invoiceDescription("IT services").priceNetto(BigDecimal.valueOf(250))
                .priceBrutto(BigDecimal.valueOf(300)).paid(false).build();

        Query query = new Query();
        query.addCriteria(Criteria.where("invoiceNumber").is(invoice.getInvoiceNumber()));

        if (mongoTemplate.find(query, Invoice.class).isEmpty()) {
            invoiceRepository.save(invoice);
        }
        else {
            System.out.println("Invoice already exists");
        }
        Invoice invoice2 = invoiceRepository.findByInvoiceNumber("1/2020").get();
        System.out.println(invoice2);
    }
}
