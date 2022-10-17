package net.dudios.invoicemanagerbackend.invoice;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/all")
    public ResponseEntity<List<Invoice>> getAllInvoices(String username) {
        return ResponseEntity.ok(invoiceService.getAllInvoices(username));
    }

    @PostMapping
    public ResponseEntity<Payload> addInvoice(Payload payload) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/invoice").toUriString());
        return ResponseEntity.created(uri).body(invoiceService.addInvoice(payload));
    }
}
