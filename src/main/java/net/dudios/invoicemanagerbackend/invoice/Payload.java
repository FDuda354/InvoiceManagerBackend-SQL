package net.dudios.invoicemanagerbackend.invoice;

import java.time.LocalDate;

public record Payload(
        Invoice invoice,

        String date,
        Long userId) {
    public Payload(Invoice invoice,  Long userId) {
        this(invoice,
                LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                userId);
    }
}
