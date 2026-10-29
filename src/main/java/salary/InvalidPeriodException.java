package salary;

/**
 * Исключение, выбрасываемое при попытке создать некорректный расчётный период
 * (например, при указании несуществующего месяца или года вне допустимого диапазона).
 */
public class InvalidPeriodException extends PayrollException {

    /**
     * Создает исключение с указанным сообщением.
     *
     * @param message описание ошибки
     */
    public InvalidPeriodException(String message) { super(message); }
}