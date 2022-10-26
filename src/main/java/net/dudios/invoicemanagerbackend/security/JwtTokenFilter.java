package net.dudios.invoicemanagerbackend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Value("${secret.key}")
    private String SECRET_KEY;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String auth = request.getHeader("Authorization");

        if(auth == null){
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =  getUsernamePasswordAuthenticationToken(auth);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request, response);

    }

    public UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String auth){
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("Admin")
                .build();
        DecodedJWT jwt = verifier.verify(auth.substring(7));

        String[] roles = jwt.getClaim("roles").asArray(String.class);

        List<SimpleGrantedAuthority> collect = Stream.of(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(jwt.getSubject(), null, collect);

    }
}
