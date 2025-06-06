package ua.wertyxa.details_check_tool_starter.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.wertyxa.details_check_tool_starter.properties.DetailsCheckProperties;
import ua.wertyxa.details_check_tool_starter.service.DetailsCheckService;

/**
 * Автоконфігурація для сервісу перевірки банківських деталей
 */
@AutoConfiguration
@ConditionalOnClass(name = {"org.springframework.web.client.RestClient", "org.jsoup.Jsoup"})
@EnableConfigurationProperties(DetailsCheckProperties.class)
public class DetailsCheckAutoConfiguration {

    /**
     * Створює та налаштовує сервіс для перевірки банківських деталей
     * @param properties властивості налаштувань
     * @return налаштований екземпляр DetailsCheckService
     */
    @Bean
    @ConditionalOnMissingBean
    public DetailsCheckService detailsCheckService(DetailsCheckProperties properties) {
        return new DetailsCheckService(properties);
    }
}
