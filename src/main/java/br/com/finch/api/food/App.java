package br.com.finch.api.food;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/*@EnableTransactionManagement
@EntityScan("br.com.curso.web.spring.devdojo.model")*/
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
