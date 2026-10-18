import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class LibraryDebtCalculator {

    static class Book {
        String type;      // "обычная" или "редкая"
        int days;
        double price;
        boolean lost;
        double charge;    // начисление
    }

     static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            int n = readN(sc);
            List<Book> books = new ArrayList<>();

            for (int i = 1; i <= n; i++) {
                System.out.println("Книга " + i + ":");
                Book b = readBook(sc);
                calculateCharge(b);
                books.add(b);
                printBookResult(i, b);
            }

            Report report = createReport(books);
            printReport(report);

            if (!askYesNo(sc, "Выполнить новый расчёт? (y/n): ")) {
                break;
            }
        }
        System.out.println("Программа завершена.");
        sc.close();
    }

    // Ввод количества книг (от 2 до 50)
    private static int readN(Scanner sc) {
        while (true) {
            System.out.print("Введите количество книг (от 2 до 50): ");
            if (sc.hasNextInt()) {
                int n = sc.nextInt();
                if (n >= 2 && n <= 50) {
                    return n;
                }
                System.out.println("Количество должно быть от 2 до 50. Попробуйте снова.");
            } else {
                System.out.println("Некорректное значение. Введите целое число.");
            }
        }
    }
    private static Book readBook(Scanner sc) {
        Book b = new Book();
        while (true) {
            System.out.print("Тип книги (обычная/редкая): ");
            String type = sc.next().trim().toLowerCase();
            if (type.equals("обычная") || type.equals("редкая")) {
                b.type = type;
                break;
            }
            System.out.println("Некорректный тип. Введите \"обычная\" или \"редкая\".");
        }
        while (true) {
            System.out.print("Количество дней просрочки: ");
            if (sc.hasNextInt()) {
                int days = sc.nextInt();
                if (days >= 0) {
                    b.days = days;
                    break;
                }
                System.out.println("Дни не могут быть отрицательными.");
            } else {
                System.out.println("Введите корректное число дней.");
                sc.next();
            }
        }
        while (true) {
            System.out.print("Цена книги (руб.): ");
            if (sc.hasNextDouble()) {
                double price = sc.nextDouble();
                if (price > 0) {
                    b.price = price;
                    break;
                }
                System.out.println("Цена должна быть положительной.");
            } else {
                System.out.println("Введите корректную цену.");
                sc.next();
            }
        }
        b.lost = askYesNo(sc, "Книга потеряна? (y/n): ");

        return b;
    }
    private static void calculateCharge(Book b) {
        if (b.lost) {
            b.charge = b.price;
        } else {
            double ratePerDay = b.type.equals("редкая") ? 50.0 : 20.0; // пример ставок
            b.charge = b.days * ratePerDay;
        }
    }

    private static void printBookResult(int index, Book b) {
        System.out.printf("Книга %d: тип=%s, дней=%d, цена=%.2f, потеряна=%b, штраф=%.2f%n",
                index, b.type, b.days, b.price, b.lost, b.charge);
    }

    private static Report createReport(List<Book> books) {
        double totalCharge = 0;
        int lostCount = 0;
        for (Book b : books) {
            totalCharge += b.charge;
            if (b.lost) lostCount++;
        }
        return new Report(books.size(), totalCharge, lostCount);
    }

    private static void printReport(Report report) {
        System.out.println("\n--- Отчёт ---");
        System.out.println("Всего книг: " + report.count);
        System.out.println("Общий штраф: " + String.format("%.2f", report.totalCharge) + " руб.");
        System.out.println("Потеряно книг: " + report.lostCount);
        System.out.println("--------------");
    }

    private static boolean askYesNo(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String answer = sc.next().trim().toLowerCase();
            if (answer.equals("y") || answer.equals("yes")) {
                return true;
            } else if (answer.equals("n") || answer.equals("no")) {
                return false;
            }
            System.out.println("Пожалуйста, введите y или n.");
        }
    }

    static class Report {
        int count;
        double totalCharge;
        int lostCount;

        Report(int count, double totalCharge, int lostCount) {
            this.count = count;
            this.totalCharge = totalCharge;
            this.lostCount = lostCount;
        }
    }
}