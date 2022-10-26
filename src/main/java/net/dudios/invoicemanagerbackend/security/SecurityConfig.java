package net.dudios.invoicemanagerbackend.security;
import lombok.AllArgsConstructor;
import net.dudios.invoicemanagerbackend.user.Role;
import net.dudios.invoicemanagerbackend.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final UserRepository userRepo;
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authorizationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers("/users/login").permitAll()
                .antMatchers("/users/register").permitAll()
                .antMatchers("/users/**").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                .antMatchers("/test/**").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                .antMatchers("/invoice/**").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                .anyRequest().authenticated();
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
