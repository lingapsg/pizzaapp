package se.apegroup.pizzaapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import se.apegroup.pizzaapp.configuration.CustomUserDetails;
import se.apegroup.pizzaapp.infrastructure.jdbc.repository.UserRepository;

@SpringBootApplication(scanBasePackages = "se.apegroup")
public class PizzaappApplication {

    public static void main(String[] args) {
        SpringApplication.run(PizzaappApplication.class, args);
    }

    @Autowired
    public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository userRepository) throws Exception {
        builder.userDetailsService(username -> new CustomUserDetails(userRepository.findByUsername(username)));
    }
}
