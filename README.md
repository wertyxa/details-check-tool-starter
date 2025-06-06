# Details Check Tool Spring Boot Starter

Бібліотека для перевірки банківських деталей (карток та IBAN), яка може бути легко інтегрована у Spring Boot проекти.

## Функціональність

- Перевірка номерів банківських карток (BIN перевірка)
- Перевірка IBAN (валідація за алгоритмом)

## Встановлення

### Maven

```xml
<dependency>
    <groupId>ua.wertyxa</groupId>
    <artifactId>details-check-tool-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### Gradle

```groovy
implementation 'ua.wertyxa:details-check-tool-spring-boot-starter:0.0.1-SNAPSHOT'
```

## Використання

Після додавання залежності у ваш проект, Spring Boot автоматично налаштує всі необхідні компоненти. Для використання сервісу просто внедріть його у ваш код:

```java
@Service
public class YourService {
    
    private final DetailsCheckService detailsCheckService;
    
    public YourService(DetailsCheckService detailsCheckService) {
        this.detailsCheckService = detailsCheckService;
    }
    
    public void yourMethod() {
        // Перевірка номера банківської картки
        CardCheckResult cardResult = detailsCheckService.checkCard("4111111111111111");
        System.out.println("Банківська карта: " + cardResult);
        
        // Перевірка IBAN
        IbanCheckResult ibanResult = detailsCheckService.checkIban("UA213223130000026007233566001");
        System.out.println("IBAN: " + ibanResult);
    }
}
```

## Налаштування

Ви можете налаштувати поведінку бібліотеки у вашому `application.properties` або `application.yml`:

### application.properties

```properties
details.check.card-check-url=https://decode.org.ua/card
details.check.timeout=5000
details.check.enable-detailed-logs=false
```

### application.yml

```yaml
details:
  check:
    card-check-url: https://decode.org.ua/card
    timeout: 5000
    enable-detailed-logs: false
```

## Розширення

Якщо вам потрібно змінити поведінку сервісу, ви можете створити власну реалізацію `DetailsCheckService` і зареєструвати її як біну Spring. Автоконфігурація не створюватиме біну за замовчуванням, якщо вже існує біна такого типу.

```java
@Bean
public DetailsCheckService customDetailsCheckService(DetailsCheckProperties properties) {
    return new CustomDetailsCheckService(properties);
}
```

## Ліцензія

Цей проект розповсюджується під ліцензією MIT.
