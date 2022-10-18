package net.dudios.invoicemanagerbackend.invoice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/invoice")
@Slf4j
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/all")
    public ResponseEntity<Set<Invoice>> getAllInvoices(Long userId) {
        return ResponseEntity.ok(invoiceService.getAllInvoices(userId));
    }

    @PostMapping
    public ResponseEntity<Payload> addInvoice(@RequestBody Payload payload) throws IOException {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/invoice").toUriString());
        return ResponseEntity.created(uri).body(invoiceService.addInvoice(payload));
    }

    @PostMapping("/PDF")
    public ResponseEntity<?> addInvoicePDF(@RequestParam("file") MultipartFile file,@RequestParam Long invoiceId) throws IOException {
        invoiceService.addInvoicePDF(file,invoiceId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/PDF/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@RequestParam Long invoiceId, HttpServletRequest request) {

        Invoice invoice = invoiceService.getInvoicePDF(invoiceId);
        String fileName = invoice.getTitle();

        Resource resource = new ByteArrayResource(invoice.getData());
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.error("Could not determine file type.");
        }
        if(contentType == null) {
            String type = fileName.substring(fileName.lastIndexOf("."));
            contentType = "application/"+type.substring(1);
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
