package salary;

import java.time.LocalDate;

/**
 * Начисление по больничному листу.
 * Рассчитывается как: среднедневной заработок × количество дней нетрудоспособности.
 */
public final class SickLeave extends Accrual {

    /**
     * Создает начисление по больничному.
     *
     * @param date дата начисления
     * @param avgDailyKopecks среднедневной заработок в копейках
     * @param days количество дней больничного
     * @throws IllegalArgumentException если параметры некорректны
     */
    public SickLeave(LocalDate date, long avgDailyKopecks, int days) {
        super(date, avgDailyKopecks * days);
        if (avgDailyKopecks < 0 || days <= 0)
            throw new IllegalArgumentException("Некорректные параметры больничного");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() { return "Больничный"; }
}
