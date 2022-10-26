package net.dudios.invoicemanagerbackend.invoice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.dudios.invoicemanagerbackend.user.AppUser;

import javax.persistence.*;
import java.math.BigDecimal;

import static javax.persistence.GenerationType.AUTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="INVOICES")
public class Invoice {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private AppUser appUser;

    private String companyName;
    private String companyAddress;
    private String companyNIP;

    private String invoiceNumber;
    private String invoiceDate;

    private String invoiceDescription;
    private String title;

    private BigDecimal priceNetto;
    private BigDecimal priceBrutto;

    private boolean paid;

    @Lob
    private byte[] data;

}
