package net.dudios.invoicemanagerbackend.security;


public record AuthRequest (
        String username,
        String password) {


}
