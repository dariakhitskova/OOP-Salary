package salary;

/**
 * Базовое проверяемое исключение домена расчёта заработной платы.
 * Используется для сигнализации о нарушении бизнес-правил.
 */
public class PayrollException extends Exception {

    /**
     * Создает исключение с указанным сообщением.
     *
     * @param message описание ошибки
     */
    public PayrollException(String message) { super(message); }

    /**
     * Создает исключение с указанным сообщением и причиной.
     *
     * @param message описание ошибки
     * @param cause первопричина (исходное исключение)
     */
    public PayrollException(String message, Throwable cause) { super(message, cause); }
}