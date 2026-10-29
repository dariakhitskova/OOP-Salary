package salary;

import java.time.LocalDate;

/**
 * Удержание аванса (ранее выплаченной части заработной платы).
 */
public final class Advance extends Deduction {

    /**
     * Создает удержание аванса.
     *
     * @param date дата удержания
     * @param amountKopecks сумма аванса в копейках
     */
    public Advance(LocalDate date, long amountKopecks) {
        super(date, amountKopecks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() { return "Аванс"; }
}