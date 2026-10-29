package salary;

import java.time.LocalDate;

/**
 * Демонстрационный класс для запуска программы.
 * Показывает работу базового и повышенного уровней (расчёт, перерасчёт, исключения).
 */
public final class Main {

    private Main() {}

    /**
     * Точка входа в программу.
     *
     * @param args аргументы командной строки (не используются)
     * @throws Exception при возникновении ошибок расчёта
     */
    public static void main(String[] args) throws Exception {
        Employee ivanov = new Employee("E-001", "Иванов Иван");
        Employee petrova = new Employee("E-002", "Петрова Мария");

        PayrollPeriod jan = PayrollPeriod.of(2025, 1);
        PayrollPeriod feb = jan.next();

        PayrollCalculator calc = new PayrollCalculator();
        calc.addTaxRate(new RatePeriod(LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31), 130));

        LocalDate d1 = LocalDate.of(2025, 1, 31);
        calc.addAccrual(new Salary(d1, 800000));
        calc.addAccrual(new Bonus(d1, 150000));
        calc.addAccrual(new NightSurcharge(d1, 50000, 20));
        calc.addAccrual(new VacationPay(d1, 270000, 3));
        calc.addAccrual(new SickLeave(d1, 200000, 2));
        calc.addDeduction(new Advance(LocalDate.of(2025, 1, 15), 300000));
        calc.addDeduction(new WritOfExecution(d1, 50000));

        LocalDate d2 = LocalDate.of(2025, 2, 28);
        calc.addAccrual(new Salary(d2, 800000));
        calc.addAccrual(new Bonus(d2, 200000));
        calc.addAccrual(new NightSurcharge(d2, 50000, 10));
        calc.addDeduction(new Advance(LocalDate.of(2025, 2, 14), 300000));

        Payslip janSlip = calc.calculate(ivanov, jan);
        Payslip febSlip = calc.calculate(ivanov, feb);
        print("ЯНВАРЬ — " + ivanov, janSlip);
        print("ФЕВРАЛЬ — " + ivanov, febSlip);

        System.out.println("\n=== Сравнение январского и февральского листков ===");
        for (String s : janSlip.diff(febSlip)) System.out.println("  " + s);

        PayrollCalculator bad = new PayrollCalculator();
        bad.addTaxRate(new RatePeriod(LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31), 130));
        bad.addAccrual(new Salary(d1, 800000));
        bad.addAccrual(new Bonus(d1, 500000));
        bad.addDeduction(new Advance(LocalDate.of(2025, 1, 15), 300000));
        Payslip wrong = bad.calculate(ivanov, jan);
        long delta = bad.recalculateBackdated(ivanov, wrong);

        System.out.println("\n=== Перерасчёт января задним числом ===");
        System.out.println("  Разница для выноса в текущий период: " + delta + " коп.");

        System.out.println("\n=== Демонстрация исключений ===");
        try {
            new Employee("", "Без номера");
        } catch (IllegalArgumentException e) {
            System.out.println("  IllegalArgumentException: " + e.getMessage());
        }

        try {
            PayrollPeriod.of(2025, 13);
        } catch (InvalidPeriodException e) {
            System.out.println("  InvalidPeriodException: " + e.getMessage());
        }

        PayrollCalculator over = new PayrollCalculator();
        over.addAccrual(new Salary(d1, 100000));
        over.addDeduction(new Advance(d1, 90000));
        try {
            over.calculate(ivanov, jan);
        } catch (DeductionLimitExceededException e) {
            System.out.println("  DeductionLimitExceededException: " + e.getMessage());
        }

        System.out.println("\n=== equals/hashCode ===");
        Employee sameNumber = new Employee("E-001", "Другое имя");
        System.out.println("  equals: " + ivanov.equals(sameNumber));
        System.out.println("  hashCode равны: " + (ivanov.hashCode() == sameNumber.hashCode()));
    }

    private static void print(String title, Payslip slip) {
        System.out.println("\n=== " + title + " ===");
        for (PayslipLine l : slip.lines())
            System.out.printf("  %-28s %10d коп.%n", l.name(), l.amountKopecks());
        System.out.println("  Начислено: " + slip.totalAccruals() + " коп.");
        System.out.println("  Удержано:  " + slip.totalDeductions() + " коп.");
        System.out.println("  К выплате: " + slip.netPay() + " коп.");
    }
}