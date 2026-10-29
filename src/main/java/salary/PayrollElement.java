package salary;

import java.time.LocalDate;

/**
 * Общий контракт для всех элементов расчёта заработной платы.
 * Объединяет две независимые иерархии: начисления и удержания.
 */
public interface PayrollElement {

    /**
     * Возвращает название элемента (для отображения в расчётном листке).
     *
     * @return название элемента
     */
    String name();

    /**
     * Возвращает дату применения элемента.
     *
     * @return дата начисления или удержания
     */
    LocalDate date();

    /**
     * Возвращает сумму элемента в копейках.
     *
     * @return сумма в копейках
     */
    long amountKopecks();

    /**
     * Проверяет корректность данных элемента.
     *
     * @throws PayrollException если сумма элемента отрицательная
     */
    default void validate() throws PayrollException {
        if (amountKopecks() < 0) {
            throw new PayrollException("Отрицательная сумма: " + name());
        }
    }
}