package salary;

import java.time.LocalDate;

/**
 * Базовый абстрактный класс для всех видов удержаний.
 * Удержания вычитаются из базы начислений после расчёта налога.
 */
public abstract class Deduction implements PayrollElement {
    private final LocalDate date;
    private final long amountKopecks;

    /**
     * Создает базовое удержание.
     *
     * @param date дата применения удержания
     * @param amountKopecks сумма в копейках
     */
    protected Deduction(LocalDate date, long amountKopecks) {
        this.date = date;
        this.amountKopecks = amountKopecks;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate date() { return date; }

    /**
     * {@inheritDoc}
     */
    @Override
    public long amountKopecks() { return amountKopecks; }
}