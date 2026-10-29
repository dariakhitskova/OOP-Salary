package salary;

import java.time.LocalDate;

/**
 * Базовый абстрактный класс для всех видов начислений.
 * Начисления увеличивают базу для расчета налога и итоговой суммы к выплате.
 */
public abstract class Accrual implements PayrollElement {
    private final LocalDate date;
    private final long amountKopecks;

    /**
     * Создает базовое начисление.
     *
     * @param date дата применения начисления
     * @param amountKopecks сумма в копейках
     */
    protected Accrual(LocalDate date, long amountKopecks) {
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