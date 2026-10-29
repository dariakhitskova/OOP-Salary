package salary;

import java.time.LocalDate;

/**
 * Начисление в виде премии.
 */
public final class Bonus extends Accrual {

    /**
     * Создает начисление премии.
     *
     * @param date дата начисления
     * @param amountKopecks сумма премии в копейках
     */
    public Bonus(LocalDate date, long amountKopecks) {
        super(date, amountKopecks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() { return "Премия"; }
}