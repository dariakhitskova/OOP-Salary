package salary;

import java.time.LocalDate;

/**
 * Начисление отпускных.
 * Рассчитывается как: среднедневной заработок × количество дней отпуска.
 */
public final class VacationPay extends Accrual {

    /**
     * Создает начисление отпускных.
     *
     * @param date дата начисления
     * @param avgDailyKopecks среднедневной заработок в копейках
     * @param days количество дней отпуска
     * @throws IllegalArgumentException если параметры некорректны
     */
    public VacationPay(LocalDate date, long avgDailyKopecks, int days) {
        super(date, avgDailyKopecks * days);
        if (avgDailyKopecks < 0 || days <= 0)
            throw new IllegalArgumentException("Некорректные параметры отпускных");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() { return "Отпускные"; }
}