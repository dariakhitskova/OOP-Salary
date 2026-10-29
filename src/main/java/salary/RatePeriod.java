package salary;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Класс, описывающий период действия определенной ставки (например, налоговой).
 */
public final class RatePeriod {
    private final LocalDate from;
    private final LocalDate to;
    private final long ratePermille;

    /**
     * Создает период действия ставки.
     *
     * @param from начало периода
     * @param to конец периода (включительно)
     * @param ratePermille значение ставки в промилле
     * @throws IllegalArgumentException если конец раньше начала или ставка отрицательна
     */
    public RatePeriod(LocalDate from, LocalDate to, long ratePermille) {
        this.from = Objects.requireNonNull(from, "from");
        this.to = Objects.requireNonNull(to, "to");
        if (to.isBefore(from)) throw new IllegalArgumentException("Конец раньше начала");
        if (ratePermille < 0) throw new IllegalArgumentException("Отрицательная ставка");
        this.ratePermille = ratePermille;
    }

    /**
     * Проверяет, попадает ли дата в период действия ставки.
     *
     * @param date проверяемая дата
     * @return true, если дата входит в период
     */
    public boolean covers(LocalDate date) {
        return !date.isBefore(from) && !date.isAfter(to);
    }

    /**
     * Возвращает дату начала периода.
     *
     * @return начало периода
     */
    public LocalDate from() { return from; }

    /**
     * Возвращает дату окончания периода.
     *
     * @return конец периода
     */
    public LocalDate to() { return to; }

    /**
     * Возвращает значение ставки.
     *
     * @return ставка в промилле
     */
    public long ratePermille() { return ratePermille; }

    /**
     * Сравнивает периоды ставок.
     *
     * @param o объект для сравнения
     * @return true, если все поля совпадают
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof RatePeriod r && from.equals(r.from) && to.equals(r.to)
                && ratePermille == r.ratePermille;
    }

    /**
     * Возвращает хеш-код периода.
     *
     * @return хеш-код
     */
    @Override
    public int hashCode() { return Objects.hash(from, to, ratePermille); }
}