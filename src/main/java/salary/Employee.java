package salary;

/**
 * Класс, представляющий сотрудника.
 * Ключом равенства (идентификатором) выступает табельный номер.
 */
public final class Employee {
    private final String personnelNumber;
    private final String fullName;

    /**
     * Создает сотрудника.
     *
     * @param personnelNumber табельный номер (уникальный идентификатор)
     * @param fullName ФИО сотрудника
     * @throws IllegalArgumentException если табельный номер или ФИО пусты
     */
    public Employee(String personnelNumber, String fullName) {
        if (personnelNumber == null || personnelNumber.isBlank())
            throw new IllegalArgumentException("Пустой табельный номер");
        if (fullName == null || fullName.isBlank())
            throw new IllegalArgumentException("Пустое ФИО");
        this.personnelNumber = personnelNumber;
        this.fullName = fullName;
    }

    /**
     * Возвращает табельный номер.
     *
     * @return табельный номер
     */
    public String personnelNumber() { return personnelNumber; }

    /**
     * Возвращает ФИО.
     *
     * @return ФИО сотрудника
     */
    public String fullName() { return fullName; }

    /**
     * Сравнивает сотрудников по табельному номеру.
     *
     * @param o объект для сравнения
     * @return true, если табельные номера совпадают
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Employee e && personnelNumber.equals(e.personnelNumber);
    }

    /**
     * Возвращает хеш-код на основе табельного номера.
     *
     * @return хеш-код
     */
    @Override
    public int hashCode() { return personnelNumber.hashCode(); }

    /**
     * Возвращает строковое представление сотрудника.
     *
     * @return строка вида "ФИО [табельный номер]"
     */
    @Override
    public String toString() { return fullName + " [" + personnelNumber + "]"; }
}