package salary;

import java.time.LocalDate;

/**
 * Удержание налога на доходы физических лиц (НДФЛ).
 * Ставка задается в промилле (например, 130 соответствует 13%).
 */
public final class IncomeTax extends Deduction {
    private final long ratePermille;

    private IncomeTax(LocalDate date, long amountKopecks, long ratePermille) {
        super(date, amountKopecks);
        this.ratePermille = ratePermille;
    }

    /**
     * Фабричный метод для создания удержания НДФЛ.
     * Рассчитывает сумму налога от базы с использованием целочисленного округления.
     *
     * @param date дата удержания
     * @param baseKopecks налоговая база (сумма начислений) в копейках
     * @param ratePermille налоговая ставка в промилле
     * @return экземпляр удержания
     * @throws IllegalArgumentException если база или ставка отрицательны
     */
    public static IncomeTax of(LocalDate date, long baseKopecks, long ratePermille) {
        if (baseKopecks < 0) throw new IllegalArgumentException("Отрицательная база");
        if (ratePermille < 0) throw new IllegalArgumentException("Отрицательная ставка");
        long amount = Math.addExact(Math.multiplyExact(baseKopecks, ratePermille), 500) / 1000;
        return new IncomeTax(date, amount, ratePermille);
    }

    /**
     * Возвращает примененную налоговую ставку.
     *
     * @return ставка в промилле
     */
    public long ratePermille() { return ratePermille; }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() { return "НДФЛ (" + ratePermille / 10.0 + "%)"; }
}