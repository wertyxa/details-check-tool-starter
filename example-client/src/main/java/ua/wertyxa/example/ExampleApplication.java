package ua.wertyxa.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ua.wertyxa.details_check_tool_starter.model.CardCheckResult;
import ua.wertyxa.details_check_tool_starter.model.IbanCheckResult;
import ua.wertyxa.details_check_tool_starter.service.DetailsCheckService;

/**
 * Приклад використання details-check-tool-spring-boot-starter
 */
@SpringBootApplication
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

    /**
     * Демонстрація використання сервісу перевірки деталей
     */
    @Bean
    public CommandLineRunner demo(DetailsCheckService detailsCheckService) {
        return args -> {
            System.out.println("====== Перевірка банківської картки ======");
            // Перевірка Visa картки
            CardCheckResult visaResult = detailsCheckService.checkCard("4149 4991 1234 5678");
            System.out.println("Результат перевірки VISA: " + visaResult);
            
            // Перевірка MasterCard картки
            CardCheckResult masterCardResult = detailsCheckService.checkCard("5168 7450 1234 5678");
            System.out.println("Результат перевірки MasterCard: " + masterCardResult);
            
            System.out.println("\n====== Перевірка IBAN ======");
            // Перевірка валідного українського IBAN
            IbanCheckResult validIbanResult = detailsCheckService.checkIban("UA213223130000026007233566001");
            System.out.println("Перевірка валідного IBAN: " + validIbanResult);
            
            // Перевірка невалідного IBAN
            IbanCheckResult invalidIbanResult = detailsCheckService.checkIban("UA313223130000026007233566001");
            System.out.println("Перевірка невалідного IBAN: " + invalidIbanResult);
        };
    }
}
