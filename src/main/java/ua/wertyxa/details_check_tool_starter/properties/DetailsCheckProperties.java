package ua.wertyxa.details_check_tool_starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Властивості для налаштування сервісу перевірки банківських деталей
 */
@Data
@ConfigurationProperties(prefix = "details.check")
public class DetailsCheckProperties {
    
    /**
     * URL для перевірки банківських карток
     */
    private String cardCheckUrl = "https://decode.org.ua/card";
    
    /**
     * Максимальний час очікування відповіді від сервісу (в мілісекундах)
     */
    private int timeout = 5000;
    
    /**
     * Увімкнути/вимкнути логування деталей запитів
     */
    private boolean enableDetailedLogs = false;
}
