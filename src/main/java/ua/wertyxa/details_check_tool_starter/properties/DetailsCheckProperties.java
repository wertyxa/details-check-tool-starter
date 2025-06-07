package ua.wertyxa.details_check_tool_starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties for configuring the bank details check service
 */
@Data
@ConfigurationProperties(prefix = "details.check")
public class DetailsCheckProperties {
    
    /**
     * Maximum time to wait for a response from the service (in milliseconds)
     */
    private int timeout = 5000;
    
    /**
     * Enable or disable detailed logging of request details
     */
    private boolean enableDetailedLogs = false;
}