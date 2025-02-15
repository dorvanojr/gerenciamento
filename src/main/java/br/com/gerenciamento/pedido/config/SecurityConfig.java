package br.com.gerenciamento.pedido.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/orders/**").authenticated() // Protege os endpoints /orders/**
                .anyRequest().permitAll() // Permite acesso a todos os outros endpoints
                .and()
            .httpBasic(); // Habilita autenticação básica
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        System.out.println("Configurando usuário em memória..."); // Log para depuração

        // Codifica a senha antes de criar o usuário
        String encodedPassword = passwordEncoder().encode("senha123");

        UserDetails user = User.builder()
            .username("admin")
            .password(encodedPassword) // Senha codificada
            .roles("USER")
            .build();

        // Retorna um gerenciador de usuários em memória com o usuário criado
        return new InMemoryUserDetailsManager(user);
    }
}
