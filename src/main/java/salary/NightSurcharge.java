package salary;

import java.time.LocalDate;

/**
 * Доплата за работу в ночные часы.
 * Рассчитывается как: часовая ставка × количество часов × коэффициент 0.4.
 * Округление производится математически (half-up) без потери точности.
 */
public final class NightSurcharge extends Accrual {
    private static final long COEFFICIENT_NUM = 2;
    private static final long COEFFICIENT_DEN = 5;

    private final int nightHours;

    /**
     * Создает начисление за ночные часы.
     *
     * @param date дата начисления
     * @param hourlyRateKopecks часовая тарифная ставка в копейках
     * @param nightHours количество отработанных ночных часов
     * @throws IllegalArgumentException если ставка или часы отрицательны
     */
    public NightSurcharge(LocalDate date, long hourlyRateKopecks, int nightHours) {
        super(date, compute(hourlyRateKopecks, nightHours));
        if (hourlyRateKopecks < 0 || nightHours < 0)
            throw new IllegalArgumentException("Отрицательные параметры");
        this.nightHours = nightHours;
    }

    private static long compute(long rate, int hours) {
        long product = Math.multiplyExact(rate, (long) hours);
        long numerator = Math.multiplyExact(product, COEFFICIENT_NUM);
        return Math.addExact(numerator, COEFFICIENT_DEN / 2) / COEFFICIENT_DEN;
    }

    /**
     * Возвращает количество ночных часов.
     *
     * @return количество часов
     */
    public int nightHours() { return nightHours; }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() { return "Ночные (" + nightHours + " ч)"; }
}