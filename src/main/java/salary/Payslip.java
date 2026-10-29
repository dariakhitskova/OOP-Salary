package salary;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Класс, представляющий расчётный листок сотрудника за период.
 * Содержит расшифровку по строкам (начисления и удержания).
 */
public final class Payslip {
    private final Employee employee;
    private final PayrollPeriod period;
    private final List<PayslipLine> lines;
    private final LocalDateTime createdAt;

    /**
     * Создает расчётный листок.
     *
     * @param employee сотрудник
     * @param period расчётный период
     * @param lines строки листка
     * @param createdAt дата и время формирования листка
     */
    public Payslip(Employee employee, PayrollPeriod period,
                   List<PayslipLine> lines, LocalDateTime createdAt) {
        this.employee = Objects.requireNonNull(employee);
        this.period = Objects.requireNonNull(period);
        this.lines = List.copyOf(lines);
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    /**
     * Возвращает сотрудника.
     *
     * @return сотрудник
     */
    public Employee employee() { return employee; }

    /**
     * Возвращает период.
     *
     * @return расчётный период
     */
    public PayrollPeriod period() { return period; }

    /**
     * Возвращает неизменяемый список строк листка.
     *
     * @return список строк
     */
    public List<PayslipLine> lines() { return lines; }

    /**
     * Возвращает дату и время создания.
     *
     * @return дата и время
     */
    public LocalDateTime createdAt() { return createdAt; }

    /**
     * Суммирует все начисления.
     *
     * @return сумма начислений в копейках
     */
    public long totalAccruals() {
        return lines.stream()
                .filter(l -> l.kind() == PayslipLine.Kind.ACCRUAL)
                .mapToLong(PayslipLine::amountKopecks).sum();
    }

    /**
     * Суммирует все удержания.
     *
     * @return сумма удержаний в копейках
     */
    public long totalDeductions() {
        return lines.stream()
                .filter(l -> l.kind() == PayslipLine.Kind.DEDUCTION)
                .mapToLong(PayslipLine::amountKopecks).sum();
    }

    /**
     * Рассчитывает сумму к выплате (начисления минус удержания).
     *
     * @return сумма к выплате в копейках
     */
    public long netPay() { return totalAccruals() - totalDeductions(); }

    /**
     * Сравнивает текущий листок с другой версией и возвращает различия.
     *
     * @param other другая версия листка
     * @return список строк с описанием различий
     */
    public List<String> diff(Payslip other) {
        Objects.requireNonNull(other);
        Map<String, PayslipLine> oldMap = indexByKey(lines);
        Map<String, PayslipLine> newMap = indexByKey(other.lines);
        List<String> diffs = new ArrayList<>();

        for (var e : newMap.entrySet()) {
            PayslipLine oldLine = oldMap.get(e.getKey());
            PayslipLine newLine = e.getValue();
            long oldAmount = oldLine == null ? 0 : oldLine.amountKopecks();
            if (oldAmount != newLine.amountKopecks()) {
                diffs.add(String.format("%s: %d → %d",
                        newLine.name(), oldAmount, newLine.amountKopecks()));
            }
        }
        for (var e : oldMap.entrySet()) {
            if (!newMap.containsKey(e.getKey())) {
                PayslipLine oldLine = e.getValue();
                diffs.add(String.format("%s: %d → 0 (удалено)",
                        oldLine.name(), oldLine.amountKopecks()));
            }
        }
        return List.copyOf(diffs);
    }

    private static Map<String, PayslipLine> indexByKey(List<PayslipLine> src) {
        Map<String, PayslipLine> map = new LinkedHashMap<>();
        for (PayslipLine l : src) {
            map.put(l.name() + "|" + l.kind(), l);
        }
        return map;
    }
}