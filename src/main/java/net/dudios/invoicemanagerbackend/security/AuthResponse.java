package net.dudios.invoicemanagerbackend.security;

public record AuthResponse (
        String token,
        String username,
        String userId){
}
