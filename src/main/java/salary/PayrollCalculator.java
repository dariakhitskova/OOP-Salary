package salary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Основной движок для расчёта заработной платы.
 * Применяет начисления, затем высчитывает НДФЛ, затем удержания с проверкой лимита.
 * Следует принципу открытости-закрытости (OCP): новые виды начислений/удержаний
 * не требуют изменения этого класса.
 */
public final class PayrollCalculator {

    private static final long DEFAULT_TAX_PERMILLE = 130;
    private static final long MAX_DEDUCTION_SHARE_PERMILLE = 500;

    private final List<Accrual> accruals = new ArrayList<>();
    private final List<Deduction> deductions = new ArrayList<>();
    private final List<RatePeriod> taxRates = new ArrayList<>();

    /**
     * Добавляет начисление в расчёт.
     *
     * @param a начисление
     */
    public void addAccrual(Accrual a) { accruals.add(Objects.requireNonNull(a)); }

    /**
     * Добавляет удержание в расчёт.
     *
     * @param d удержание
     */
    public void addDeduction(Deduction d) { deductions.add(Objects.requireNonNull(d)); }

    /**
     * Добавляет период действия налоговой ставки.
     *
     * @param r период действия
     */
    public void addTaxRate(RatePeriod r) { taxRates.add(Objects.requireNonNull(r)); }

    /**
     * Производит полный расчёт листка за указанный период.
     *
     * @param employee сотрудник
     * @param period расчётный период
     * @return сформированный расчётный листок
     * @throws PayrollException при нарушении лимита удержаний
     */
    public Payslip calculate(Employee employee, PayrollPeriod period) throws PayrollException {
        List<PayslipLine> lines = new ArrayList<>();

        long accrualTotal = 0;
        for (Accrual a : accruals) {
            if (!period.contains(a.date())) continue;
            a.validate();
            lines.add(new PayslipLine(a.name(), PayslipLine.Kind.ACCRUAL, a.amountKopecks()));
            accrualTotal += a.amountKopecks();
        }

        long taxRate = rateFor(period);
        IncomeTax tax = IncomeTax.of(period.end(), accrualTotal, taxRate);
        lines.add(new PayslipLine(tax.name(), PayslipLine.Kind.DEDUCTION, tax.amountKopecks()));

        long otherDed = 0;
        for (Deduction d : deductions) {
            if (!period.contains(d.date())) continue;
            d.validate();
            otherDed += d.amountKopecks();
            lines.add(new PayslipLine(d.name(), PayslipLine.Kind.DEDUCTION, d.amountKopecks()));
        }

        long totalDed = tax.amountKopecks() + otherDed;
        long limit = Math.round(accrualTotal * MAX_DEDUCTION_SHARE_PERMILLE / 1000.0);
        if (accrualTotal > 0 && totalDed > limit) {
            throw new DeductionLimitExceededException(String.format(
                    "Удержания %d коп. превышают лимит %d коп.", totalDed, limit));
        }

        return new Payslip(employee, period, lines, LocalDateTime.now());
    }

    private long rateFor(PayrollPeriod period) {
        LocalDate day = period.end();
        return taxRates.stream()
                .filter(r -> r.covers(day))
                .mapToLong(RatePeriod::ratePermille)
                .findFirst()
                .orElse(DEFAULT_TAX_PERMILLE);
    }

    /**
     * Выполняет перерасчёт задним числом.
     * Возвращает разницу между новым и старым расчётом, которая выносится в текущий период.
     *
     * @param employee сотрудник
     * @param oldPayslip старая версия расчётного листка
     * @return разница в копейках
     * @throws PayrollException при ошибке расчёта
     */
    public long recalculateBackdated(Employee employee, Payslip oldPayslip) throws PayrollException {
        Payslip fresh = calculate(employee, oldPayslip.period());
        return fresh.netPay() - oldPayslip.netPay();
    }
}