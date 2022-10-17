package net.dudios.invoicemanagerbackend.invoice;

import lombok.AllArgsConstructor;
import net.dudios.invoicemanagerbackend.user.AppUser;
import net.dudios.invoicemanagerbackend.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InvoiceService {

    private final UserRepository userRepository;
    public List<Invoice> getAllInvoices(String username) {
        return userRepository.findByUsername(username).getInvoices();
    }

    Payload addInvoice(Payload payload) {
        AppUser user = userRepository.findByUsername(payload.username());
        Invoice invoice = payload.invoice();
        invoice.setInvoicePDF(payload.invoicePDF());

        user.getInvoices().add(invoice);
        return payload;
    }

}
