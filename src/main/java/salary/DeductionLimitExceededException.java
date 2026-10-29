package salary;

/**
 * Исключение, выбрасываемое в случае, если сумма всех удержаний
 * превысила допустимую долю (лимит) от базы начислений.
 */
public class DeductionLimitExceededException extends PayrollException {

    /**
     * Создает исключение с указанным сообщением.
     *
     * @param message описание ошибки
     */
    public DeductionLimitExceededException(String message) { super(message); }
}