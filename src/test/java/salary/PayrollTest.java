package salary;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class PayrollTest {

    private final LocalDate D = LocalDate.of(2025, 1, 31);
    private final Employee EMP = new Employee("E-1", "Тест");

    @Test
    void salaryMinusTax() throws Exception {
        var calc = new PayrollCalculator();
        calc.addAccrual(new Salary(D, 100_000));
        var slip = calc.calculate(EMP, PayrollPeriod.of(2025, 1));
        assertEquals(100_000, slip.totalAccruals());
        assertEquals(13_000, slip.totalDeductions());
        assertEquals(87_000, slip.netPay());
    }

    @Test
    void multipleAccruals() throws Exception {
        var calc = new PayrollCalculator();
        calc.addAccrual(new Salary(D, 100_000));
        calc.addAccrual(new Bonus(D, 20_000));
        var slip = calc.calculate(EMP, PayrollPeriod.of(2025, 1));
        assertEquals(120_000, slip.totalAccruals());
    }

    @Test
    void nightSurcharge() {
        var n = new NightSurcharge(D, 10_000, 10);
        assertEquals(40_000, n.amountKopecks());
    }

    @Test
    void vacationPay() {
        var v = new VacationPay(D, 5_000, 14);
        assertEquals(70_000, v.amountKopecks());
    }

    @Test
    void outsidePeriodIgnored() throws Exception {
        var calc = new PayrollCalculator();
        calc.addAccrual(new Salary(LocalDate.of(2025, 2, 1), 100_000));
        var slip = calc.calculate(EMP, PayrollPeriod.of(2025, 1));
        assertEquals(0, slip.totalAccruals());
    }

    @Test
    void deductionLimitExceeded() {
        var calc = new PayrollCalculator();
        calc.addAccrual(new Salary(D, 100_000));
        calc.addDeduction(new Advance(D, 90_000));
        assertThrows(DeductionLimitExceededException.class,
                () -> calc.calculate(EMP, PayrollPeriod.of(2025, 1)));
    }

    @Test
    void invalidMonth() {
        assertThrows(InvalidPeriodException.class, () -> PayrollPeriod.of(2025, 13));
    }

    @Test
    void invalidYear() {
        assertThrows(InvalidPeriodException.class, () -> PayrollPeriod.of(1800, 1));
    }

    @Test
    void emptyPersonnelNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Employee("", "Тест"));
    }

    @Test
    void employeeEquality() {
        var a = new Employee("E-1", "Иванов");
        var b = new Employee("E-1", "Другое имя");
        var c = new Employee("E-2", "Иванов");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    void customTaxRate() throws Exception {
        var calc = new PayrollCalculator();
        calc.addTaxRate(new RatePeriod(LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31), 150));
        calc.addAccrual(new Salary(D, 100_000));
        var slip = calc.calculate(EMP, PayrollPeriod.of(2025, 1));
        assertEquals(15_000, slip.totalDeductions());
    }

    @Test
    void backdatedRecalculation() throws Exception {
        var calc = new PayrollCalculator();
        calc.addAccrual(new Salary(D, 100_000));
        var oldSlip = calc.calculate(EMP, PayrollPeriod.of(2025, 1));

        var updated = new PayrollCalculator();
        updated.addAccrual(new Salary(D, 120_000));
        long delta = updated.recalculateBackdated(EMP, oldSlip);

        assertEquals(17_400, delta);
    }

    @Test
    void slipDiff() throws Exception {
        var calc1 = new PayrollCalculator();
        calc1.addAccrual(new Salary(D, 100_000));
        var slip1 = calc1.calculate(EMP, PayrollPeriod.of(2025, 1));

        var calc2 = new PayrollCalculator();
        calc2.addAccrual(new Salary(D, 100_000));
        calc2.addAccrual(new Bonus(D, 5_000));
        var slip2 = calc2.calculate(EMP, PayrollPeriod.of(2025, 1));

        var diffs = slip1.diff(slip2);
        assertFalse(diffs.isEmpty());
        assertTrue(diffs.stream().anyMatch(s -> s.contains("Премия")));
    }
}