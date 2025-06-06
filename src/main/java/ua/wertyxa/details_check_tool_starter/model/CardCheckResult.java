package ua.wertyxa.details_check_tool_starter.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Результат перевірки банківської картки
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CardCheckResult {
    private String message;
    private String card;
    private String cardType;
    private String cardIssuer;
    private String cardIssuerCountry;
    private Boolean isChecksumValid;
}
