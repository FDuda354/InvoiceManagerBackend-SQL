package net.dudios.invoicemanagerbackend.invoicePDF;

import lombok.AllArgsConstructor;
import net.dudios.invoicemanagerbackend.invoice.Invoice;
import net.dudios.invoicemanagerbackend.user.AppUser;
import net.dudios.invoicemanagerbackend.user.UserRepository;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@AllArgsConstructor
public class InvoicePDFService {
    private final UserRepository userRepository;

    public void addInvoicePDF(MultipartFile file,String invoiceNumber,String username) throws IOException {

        String title = file.getOriginalFilename();
        InvoicePDF invoicePDF = new InvoicePDF(title, new Binary(BsonBinarySubType.BINARY, file.getBytes()));

        AppUser user = userRepository.findByUsername(username);
        user.getInvoices().stream().filter(i ->
                i.getInvoiceNumber().equals(invoiceNumber)).findFirst().get().setInvoicePDF(invoicePDF);
        userRepository.save(user);
    }

    public InvoicePDF getInvoicePDF(String invoiceNumber, String username) {
        InvoicePDF invoicePDF = userRepository.findByUsername(username).getInvoices().stream().filter(invoice ->
                invoice.getInvoiceNumber().equals(invoiceNumber)).findFirst().get().getInvoicePDF();
        System.out.println(invoicePDF.getImage());

        return invoicePDF;
    }
}
