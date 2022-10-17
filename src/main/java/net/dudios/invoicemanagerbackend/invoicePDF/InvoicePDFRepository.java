package net.dudios.invoicemanagerbackend.invoicePDF;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvoicePDFRepository extends MongoRepository<InvoicePDF, String> {

    InvoicePDF findByTitle(String fileName);
}
