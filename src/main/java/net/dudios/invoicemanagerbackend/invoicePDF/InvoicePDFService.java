package net.dudios.invoicemanagerbackend.invoicePDF;

import lombok.AllArgsConstructor;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@AllArgsConstructor
public class InvoicePDFService {
    private final InvoicePDFRepository invoicePDFRepository;

    public InvoicePDF addInvoicePDF(MultipartFile file) throws IOException {

        String title = file.getOriginalFilename();
        InvoicePDF invoicePDF = new InvoicePDF(title, new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        invoicePDF = invoicePDFRepository.save(invoicePDF);

        return invoicePDF;
    }

    public InvoicePDF getInvoicePDF(String title) {
        InvoicePDF invoicePDF = invoicePDFRepository.findByTitle(title);
        System.out.println(invoicePDF.getImage());

        return invoicePDF;
    }
}
