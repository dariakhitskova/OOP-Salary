package salary;

import java.time.LocalDate;

/**
 * Удержание по исполнительному листу.
 */
public final class WritOfExecution extends Deduction {

    /**
     * Создает удержание по исполнительному листу.
     *
     * @param date дата удержания
     * @param amountKopecks сумма удержания в копейках
     */
    public WritOfExecution(LocalDate date, long amountKopecks) {
        super(date, amountKopecks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() { return "Исполнительный лист"; }
}