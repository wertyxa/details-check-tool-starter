package ua.wertyxa.details_check_tool_starter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ua.wertyxa.details_check_tool_starter.config.DetailsCheckAutoConfiguration;
import ua.wertyxa.details_check_tool_starter.model.CardCheckResult;
import ua.wertyxa.details_check_tool_starter.model.IbanCheckResult;
import ua.wertyxa.details_check_tool_starter.service.DetailsCheckService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тести для перевірки функціональності DetailsCheckService
 */
@SpringBootTest
class DetailsCheckServiceTest {

    @SpringBootApplication
    @Import(DetailsCheckAutoConfiguration.class)
    static class TestApplication {
    }

    @Autowired
    private DetailsCheckService detailsCheckService;

    @Test
    void testCheckIban_validIban() {
        // Валідний український IBAN
        String validIban = "UA213223130000026007233566001";
        IbanCheckResult result = detailsCheckService.checkIban(validIban);
        
        assertNotNull(result);
        assertEquals(validIban, result.getIban());
        assertTrue(result.getIsValid());
        assertEquals("IBAN валідний", result.getMessage());
    }

    @Test
    void testCheckIban_invalidIban() {
        // Невалідний IBAN (змінена контрольна сума)
        String invalidIban = "UA313223130000026007233566001";
        IbanCheckResult result = detailsCheckService.checkIban(invalidIban);
        
        assertNotNull(result);
        assertEquals(invalidIban, result.getIban());
        assertFalse(result.getIsValid());
        assertEquals("IBAN недійсний", result.getMessage());
    }

    @Test
    void testCheckIban_emptyIban() {
        // Порожній IBAN
        String emptyIban = "";
        IbanCheckResult result = detailsCheckService.checkIban(emptyIban);
        
        assertNotNull(result);
        assertNull(result.getIban());
        assertNull(result.getIsValid());
        assertEquals("IBAN не може бути порожнім", result.getMessage());
    }
}
