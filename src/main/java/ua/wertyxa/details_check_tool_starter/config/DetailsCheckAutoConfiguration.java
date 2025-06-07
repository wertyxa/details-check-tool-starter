package ua.wertyxa.details_check_tool_starter.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ua.wertyxa.details_check_tool_starter.properties.DetailsCheckProperties;
import ua.wertyxa.details_check_tool_starter.service.DetailsCheckService;

/**
 * Auto-configuration for bank details check service
 */
@AutoConfiguration
@ConditionalOnClass(name = {"org.springframework.web.client.RestClient", "org.jsoup.Jsoup"})
@EnableConfigurationProperties(DetailsCheckProperties.class)
public class DetailsCheckAutoConfiguration {

    /**
     * Creates and configures service for bank details check
     * @param properties configuration properties
     * @return configured instance of DetailsCheckService
     */
    @Bean
    @ConditionalOnMissingBean
    public DetailsCheckService detailsCheckService(DetailsCheckProperties properties) {
        return new DetailsCheckService(properties);
    }
}