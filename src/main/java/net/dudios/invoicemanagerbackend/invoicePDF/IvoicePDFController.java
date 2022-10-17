package net.dudios.invoicemanagerbackend.invoicePDF;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/invoicePDF")
public class IvoicePDFController {

    private final InvoicePDFService invoicePDFService;

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam String invoiceNumber,
                                        @RequestParam String username) throws IOException {
       invoicePDFService.addInvoicePDF(file,invoiceNumber,username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName,
                                                 @RequestParam String invoiceNumber,
                                                 @RequestParam String username,
                                                 HttpServletRequest request) {
        InvoicePDF invoicePDF = invoicePDFService.getInvoicePDF(invoiceNumber,username);

        Resource resource = new ByteArrayResource(invoicePDF.getImage().getData());
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
