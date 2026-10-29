package salary;

import java.time.LocalDate;

/**
 * Начисление в виде фиксированного оклада.
 */
public final class Salary extends Accrual {

    /**
     * Создает начисление оклада.
     *
     * @param date дата начисления
     * @param amountKopecks сумма оклада в копейках
     */
    public Salary(LocalDate date, long amountKopecks) {
        super(date, amountKopecks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() { return "Оклад"; }
}
