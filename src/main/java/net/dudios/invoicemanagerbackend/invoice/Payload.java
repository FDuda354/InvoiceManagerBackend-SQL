package net.dudios.invoicemanagerbackend.invoice;

import net.dudios.invoicemanagerbackend.invoicePDF.InvoicePDF;

import java.time.LocalDate;

public record Payload(
        Invoice invoice,
        InvoicePDF invoicePDF,
        String date,
        String username) {
    public Payload(Invoice invoice, InvoicePDF invoicePDF, String username) {
        this(invoice, invoicePDF,
                LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                username);
    }
}
