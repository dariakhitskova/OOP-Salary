package salary;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Класс, представляющий расчётный период (конкретный месяц года).
 */
public final class PayrollPeriod {
    private final YearMonth month;

    private PayrollPeriod(YearMonth month) { this.month = month; }

    /**
     * Создает расчётный период.
     *
     * @param year год (не ранее 1900)
     * @param month номер месяца (от 1 до 12)
     * @return расчётный период
     * @throws InvalidPeriodException если год или месяц вне допустимого диапазона
     */
    public static PayrollPeriod of(int year, int month) throws InvalidPeriodException {
        if (year < 1900) throw new InvalidPeriodException("Год: " + year);
        if (month < 1 || month > 12) throw new InvalidPeriodException("Месяц: " + month);
        return new PayrollPeriod(YearMonth.of(year, month));
    }

    /**
     * Возвращает первый день периода.
     *
     * @return первая дата периода
     */
    public LocalDate start() { return month.atDay(1); }

    /**
     * Возвращает последний день периода.
     *
     * @return последняя дата периода
     */
    public LocalDate end() { return month.atEndOfMonth(); }

    /**
     * Возвращает год периода.
     *
     * @return год
     */
    public int year() { return month.getYear(); }

    /**
     * Возвращает номер месяца периода.
     *
     * @return номер месяца (1-12)
     */
    public int month() { return month.getMonthValue(); }

    /**
     * Проверяет, входит ли указанная дата в данный период.
     *
     * @param date проверяемая дата
     * @return true, если дата принадлежит периоду
     */
    public boolean contains(LocalDate date) {
        return !date.isBefore(start()) && !date.isAfter(end());
    }

    /**
     * Возвращает следующий расчётный период.
     *
     * @return следующий месяц
     * @throws InvalidPeriodException при выходе за допустимые границы
     */
    public PayrollPeriod next() throws InvalidPeriodException {
        YearMonth next = month.plusMonths(1);
        return of(next.getYear(), next.getMonthValue());
    }

    /**
     * Сравнивает периоды.
     *
     * @param o объект для сравнения
     * @return true, если периоды совпадают
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof PayrollPeriod p && month.equals(p.month);
    }

    /**
     * Возвращает хеш-код периода.
     *
     * @return хеш-код
     */
    @Override
    public int hashCode() { return month.hashCode(); }

    /**
     * Возвращает строковое представление периода в формате "ММ.гггг".
     *
     * @return строка с периодом
     */
    @Override
    public String toString() {
        return String.format("%02d.%d", month.getMonthValue(), month.getYear());
    }
}