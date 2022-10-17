package net.dudios.invoicemanagerbackend.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "invoices")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Invoice {

    @Id
    private String id;

    private String companyName;
    private String companyAddress;
    private String companyNIP;

    @Indexed(unique = true)
    private String invoiceNumber;
    private String invoiceDate;

    private String invoiceDescription;
    private BigDecimal priceNetto;
    private BigDecimal priceBrutto;

    private boolean paid;
}
