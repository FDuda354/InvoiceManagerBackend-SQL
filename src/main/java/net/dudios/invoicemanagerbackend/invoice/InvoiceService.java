package net.dudios.invoicemanagerbackend.invoice;

import lombok.AllArgsConstructor;
import net.dudios.invoicemanagerbackend.user.AppUser;
import net.dudios.invoicemanagerbackend.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class InvoiceService {

    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;

    public Iterable<Invoice> getAllInvoices(Long userId) {
        AppUser user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getInvoices();
    }

   public Payload addInvoice(Payload payload) {
        AppUser user = userRepository.findById(payload.userId()).orElseThrow(() -> new RuntimeException("User not found"));
        Invoice invoice = payload.invoice();
        invoice.setAppUser(user);
        invoiceRepository.save(invoice);
        return payload;
    }

    public Invoice getInvoice(Long invoiceId) {
       return invoiceRepository.findById(invoiceId).orElseThrow(() -> new RuntimeException("invoice not found"));
    }

    public void deleteInvoice(Long invoiceId) {
        invoiceRepository.deleteById(invoiceId);
    }

    public void addInvoicePDF(MultipartFile file,Long invoiceId) throws IOException {

        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new RuntimeException("invoice not found"));
        invoice.setTitle(file.getOriginalFilename());
        invoice.setData(file.getBytes());
        invoiceRepository.save(invoice);
    }

    public Invoice getInvoicePDF(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new RuntimeException("invoice not found"));

        return invoice;
    }
}
