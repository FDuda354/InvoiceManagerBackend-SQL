package net.dudios.invoicemanagerbackend.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.dudios.invoicemanagerbackend.invoicePDF.InvoicePDF;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    private InvoicePDF invoicePDF;

    private String invoiceDescription;
    private BigDecimal priceNetto;
    private BigDecimal priceBrutto;

    private boolean paid;

}
