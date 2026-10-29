package salary;

/**
 * Неизменяемая запись (record), представляющая одну строку в расчётном листке.
 *
 * @param name название строки
 * @param kind вид строки (начисление или удержание)
 * @param amountKopecks сумма в копейках
 */
public record PayslipLine(String name, Kind kind, long amountKopecks) {

    /**
     * Вид элемента расчёта.
     */
    public enum Kind { ACCRUAL, DEDUCTION }

    /**
     * Компактный конструктор с валидацией.
     *
     * @throws IllegalArgumentException если название пусто, вид не задан или сумма отрицательна
     */
    public PayslipLine {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Пустое название строки");
        if (kind == null)
            throw new IllegalArgumentException("Не задан вид строки");
        if (amountKopecks < 0)
            throw new IllegalArgumentException("Отрицательная сумма строки");
    }
}