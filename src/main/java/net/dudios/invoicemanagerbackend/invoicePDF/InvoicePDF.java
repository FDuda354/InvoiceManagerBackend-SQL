package net.dudios.invoicemanagerbackend.invoicePDF;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "invoicePDF")
public class InvoicePDF {

    @Id
    private String id;

    private String title;

    private Binary image;

    public InvoicePDF(String title, Binary image) {
        this.title = title;
        this.image = image;
    }
}
