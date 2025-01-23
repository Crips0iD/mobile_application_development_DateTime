import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

class DateTimeTasks {
    public static void main(String[] args) {
        // 1. Основы LocalDate и LocalTime
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        System.out.println("Текущая дата и время: " +
                currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " " +
                currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        // 2. Сравнение дат
        LocalDate date1 = LocalDate.of(2023, 1, 1);
        LocalDate date2 = LocalDate.of(2024, 1, 1);
        compareDates(date1, date2);

        // 3. Сколько дней до Нового года?
        System.out.println("Дней до Нового года: " + daysUntilNewYear(currentDate));

        // 4. Проверка високосного года
        System.out.println("2024 - високосный год? " + isLeapYear(2024));

        // 5. Подсчет выходных за месяц
        System.out.println("Выходных в январе 2024: " + countWeekends(1, 2024));

        // 6. Расчет времени выполнения метода
        measureExecutionTime(() -> {
            for (int i = 0; i < 1_000_000; i++); // пустой цикл
        });

        // 7. Форматирование и парсинг даты
        String inputDate = "15-01-2024";
        parseAndFormatDate(inputDate);

        // 8. Конвертация между часовыми поясами
        ZonedDateTime utcTime = ZonedDateTime.now(ZoneId.of("UTC"));
        convertTimeZone(utcTime, "Europe/Moscow");

        // 9. Вычисление возраста по дате рождения
        LocalDate birthDate = LocalDate.of(1995, 5, 23);
        System.out.println("Возраст: " + calculateAge(birthDate) + " лет");

        // 10. Создание календаря на месяц
        printMonthlyCalendar(1, 2024);

        // 11. Генерация случайной даты в диапазоне
        LocalDate randomDate = generateRandomDate(date1, date2);
        System.out.println("Случайная дата: " + randomDate);

        // 12. Расчет времени до заданной даты
        LocalDateTime eventDateTime = LocalDateTime.of(2024, 12, 31, 23, 59);
        timeUntilEvent(eventDateTime);

        // 13. Вычисление количества рабочих часов
        LocalDateTime workStart = LocalDateTime.of(2024, 1, 15, 9, 0);
        LocalDateTime workEnd = LocalDateTime.of(2024, 1, 15, 18, 0);
        System.out.println("Рабочих часов: " + calculateWorkingHours(workStart, workEnd));

        // 14. Конвертация даты в строку с учетом локали
        printDateWithLocale(currentDate, Locale.ENGLISH);

        // 15. Определение дня недели по дате
        System.out.println("День недели: " + getDayOfWeekInRussian(currentDate));
    }

    public static void compareDates(LocalDate date1, LocalDate date2) {
        if (date1.isBefore(date2)) {
            System.out.println("Первая дата раньше второй.");
        } else if (date1.isAfter(date2)) {
            System.out.println("Первая дата позже второй.");
        } else {
            System.out.println("Даты равны.");
        }
    }

    public static long daysUntilNewYear(LocalDate currentDate) {
        LocalDate newYear = LocalDate.of(currentDate.getYear() + 1, 1, 1);
        return ChronoUnit.DAYS.between(currentDate, newYear);
    }

    public static boolean isLeapYear(int year) {
        return Year.isLeap(year);
    }

    public static int countWeekends(int month, int year) {
        int weekends = 0;
        LocalDate date = LocalDate.of(year, month, 1);
        while (date.getMonthValue() == month) {
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                weekends++;
            }
            date = date.plusDays(1);
        }
        return weekends;
    }

    public static void measureExecutionTime(Runnable task) {
        long start = System.nanoTime();
        task.run();
        long end = System.nanoTime();
        System.out.println("Время выполнения: " + (end - start) / 1_000_000 + " мс");
    }

    public static void parseAndFormatDate(String inputDate) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate date = LocalDate.parse(inputDate, inputFormatter);
        date = date.plusDays(10);
        System.out.println("Дата через 10 дней: " + date.format(outputFormatter));
    }

    public static void convertTimeZone(ZonedDateTime utcTime, String targetZone) {
        ZonedDateTime targetTime = utcTime.withZoneSameInstant(ZoneId.of(targetZone));
        System.out.println("Временная зона " + targetZone + ": " + targetTime);
    }

    public static int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public static void printMonthlyCalendar(int month, int year) {
        LocalDate date = LocalDate.of(year, month, 1);
        System.out.println("Календарь на " + date.getMonth() + " " + year + ":");
        while (date.getMonthValue() == month) {
            System.out.println(date + " - " + (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY ? "Выходной" : "Рабочий день"));
            date = date.plusDays(1);
        }
    }

    public static LocalDate generateRandomDate(LocalDate start, LocalDate end) {
        long startEpochDay = start.toEpochDay();
        long endEpochDay = end.toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay + 1);
        return LocalDate.ofEpochDay(randomDay);
    }

    public static void timeUntilEvent(LocalDateTime eventDateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(now, eventDateTime);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        System.out.println("До события осталось: " + hours + " часов, " + minutes + " минут, " + seconds + " секунд.");
    }

    public static long calculateWorkingHours(LocalDateTime start, LocalDateTime end) {
        if (start.toLocalDate().isEqual(end.toLocalDate())) {
            return ChronoUnit.HOURS.between(start, end);
        }
        return 0; // Для простоты, рабочие дни только в пределах одного дня
    }

    public static void printDateWithLocale(LocalDate date, Locale locale) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", locale);
        System.out.println("Дата с локалью " + locale + ": " + date.format(formatter));
    }

    public static String getDayOfWeekInRussian(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        switch (dayOfWeek) {
            case MONDAY: return "Понедельник";
            case TUESDAY: return "Вторник";
            case WEDNESDAY: return "Среда";
            case THURSDAY: return "Четверг";
            case FRIDAY: return "Пятница";
            case SATURDAY: return "Суббота";
            case SUNDAY: return "Воскресенье";
            default: return "";
        }
    }
}
