package ua.wertyxa.details_check_tool_starter.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import ua.wertyxa.details_check_tool_starter.model.CardCheckResult;
import ua.wertyxa.details_check_tool_starter.model.IbanCheckResult;
import ua.wertyxa.details_check_tool_starter.properties.DetailsCheckProperties;

import java.util.logging.Logger;

/**
 * Service for checking bank details (card and IBAN)
 */
public class DetailsCheckService {
    private final RestClient restClient;
    private static final Logger logger = Logger.getLogger(DetailsCheckService.class.getName());

    public DetailsCheckService(DetailsCheckProperties properties) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(properties.getTimeout());
        requestFactory.setReadTimeout(properties.getTimeout());
        
        this.restClient = RestClient.builder()
                .baseUrl("https://decode.org.ua/card")
                .requestFactory(requestFactory)
                .build();
        
        if (properties.isEnableDetailedLogs()) {
            logger.info("DetailsCheckService created with timeout: " + properties.getTimeout() + " ms");
        }
    }

    /**
     * Check the bank card number
     * @param cardNumber the card number to check
     * @return the check result
     */
    public CardCheckResult checkCard(String cardNumber) {
        // Перевірка вхідних даних
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            CardCheckResult errorResult = new CardCheckResult();
            errorResult.setMessage("Номер картки не може бути порожнім");
            return errorResult;
        }

        // Видаляємо всі пробіли з номера картки
        String cleanCardNumber = cardNumber.replaceAll("\\s+", "");

        // Перевірка довжини номера картки
        if (!isValidCardNumberLength(cleanCardNumber)) {
            CardCheckResult errorResult = new CardCheckResult();
            errorResult.setMessage("Некоректна довжина номера картки");
            return errorResult;
        }

        // Створюємо form-data параметри
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("act", "apply");
        formData.add("numberCard", cleanCardNumber);

        // Створюємо об'єкт результату
        CardCheckResult result = new CardCheckResult();

        try {

            // Виконуємо POST запит
            String htmlResponse = restClient.post()
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve()
                    .body(String.class);

            // Парсимо HTML-відповідь, якщо вона не порожня
            if (htmlResponse != null && !htmlResponse.trim().isEmpty()) {
                parseCardResponse(htmlResponse, result);
            }
        } catch (Exception e) {
            logger.severe("Помилка при обробці запиту для картки " + cleanCardNumber + ": " + e.getMessage());
            result.setMessage("Сталася помилка при перевірці картки: " + e.getMessage());
        }

        return result;
    }

    /**
     * Check the IBAN
     * @param iban IBAN to check
     * @return the check result
     */
    public IbanCheckResult checkIban(String iban) {
        IbanCheckResult result = new IbanCheckResult();
        
        // Перевірка вхідних даних
        if (iban == null || iban.trim().isEmpty()) {
            result.setMessage("IBAN не може бути порожнім");
            return result;
        }
        
        // Видаляємо всі пробіли та переводимо в верхній регістр
        String cleanIban = iban.replaceAll("\\s+", "").toUpperCase();
        
        result.setIban(cleanIban);
        
        // Базові перевірки
        if (cleanIban.length() < 15 || cleanIban.length() > 34) {
            result.setMessage("Некоректна довжина IBAN");
            result.setIsValid(false);
            return result;
        }
        
        // Перевірка контрольної суми
        boolean isValid = validateIbanChecksum(cleanIban);
        result.setIsValid(isValid);
        
        if (isValid) {
            result.setMessage("IBAN валідний");
        } else {
            result.setMessage("IBAN недійсний");
        }
        
        return result;
    }
    
    /**
     * Validates the IBAN checksum using the standard algorithm
     * 1. Move the first 4 characters to the end of the string
     * 2. Replace all letters with numbers (A=10, B=11, ..., Z=35)
     * 3. Consider the resulting string as a large integer and calculate the remainder of the division by 97
     * 4. If the remainder is 1, the IBAN is valid
     */
    private boolean validateIbanChecksum(String iban) {
        // Переміщення перших 4 символів в кінець
        String rearranged = iban.substring(4) + iban.substring(0, 4);
        
        // Заміна літер на цифри
        StringBuilder numericIban = new StringBuilder();
        for (char ch : rearranged.toCharArray()) {
            if (Character.isLetter(ch)) {
                numericIban.append(Character.getNumericValue(ch));
            } else {
                numericIban.append(ch);
            }
        }
        
        // Обчислення залишку від ділення на 97
        // Оскільки IBAN може бути дуже довгим, ми обробляємо його частинами
        String numericString = numericIban.toString();
        long remainder = 0;
        
        for (int i = 0; i < numericString.length(); i += 9) {
            int end = Math.min(i + 9, numericString.length());
            String chunk = remainder + numericString.substring(i, end);
            remainder = Long.parseLong(chunk) % 97;
        }
        
        return remainder == 1;
    }

    /**
     * Checks if the length of the card number conforms to the standards
     * (most cards have 13-19 digits)
     */
    private boolean isValidCardNumberLength(String cardNumber) {
        int length = cardNumber.length();
        return length >= 13 && length <= 19 && cardNumber.matches("\\d+");
    }

    /**
     * Parses the HTML response and populates the result object
     */
    private void parseCardResponse(String htmlResponse, CardCheckResult result) {
        try {
            Document doc = Jsoup.parse(htmlResponse);

            // Шукаємо контейнер з результатами
            Element contentDiv = doc.selectFirst("div.entry-content");
            if (contentDiv == null) {
                result.setMessage("Не вдалося отримати дані про картку");
                return;
            }

            // Отримуємо заголовок (повідомлення)
            Element titleElement = contentDiv.selectFirst("h2");
            if (titleElement != null) {
                result.setMessage(titleElement.text());
            }

            // Знаходимо блок з результатами
            Element resultDiv = contentDiv.selectFirst("div.Result");
            if (resultDiv == null) {
                return;
            }

            // Отримуємо всі блоки з даними (кожен блок - це рядок інформації про картку)
            Elements infoBlocks = resultDiv.children();

            for (Element infoBlock : infoBlocks) {
                Elements divs = infoBlock.children();
                if (divs.size() < 2) continue;

                String fieldName = divs.get(0).text().trim();
                String fieldValue = divs.get(1).text().trim();

                switch (fieldName) {
                    case "Номер:":
                        result.setCard(fieldValue);
                        break;
                    case "Бренд:":
                        result.setCardType(fieldValue);
                        break;
                    case "Емітент:":
                        // Для емітента беремо тільки перший рядок тексту (назву банку)
                        result.setCardIssuer(divs.get(1).children().getFirst().text());
                        break;
                    case "Країна:":
                        result.setCardIssuerCountry(fieldValue);
                        break;
                    case "Контрольна сума:":
                        result.setIsChecksumValid(fieldValue.equals("Збігається"));
                        break;
                }
            }
        } catch (Exception e) {
            logger.severe("Error parsing HTML: " + e.getMessage());
            result.setMessage("Error processing response: " + e.getMessage());
        }
    }
}
