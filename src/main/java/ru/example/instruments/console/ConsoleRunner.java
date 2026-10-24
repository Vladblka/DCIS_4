package ru.example.instruments.console;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.example.instruments.model.MusicalInstrument;
import ru.example.instruments.service.MusicalInstrumentService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleRunner implements CommandLineRunner {
    private final MusicalInstrumentService service;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleRunner(MusicalInstrumentService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {
        System.out.println("=== Учет музыкальных инструментов ===");
        while (true) {
            printMenu();
            String command = scanner.nextLine().trim();
            try {
                switch (command) {
                    case "1" -> add();
                    case "2" -> print(service.all());
                    case "3" -> update();
                    case "4" -> delete();
                    case "5" -> search();
                    case "0" -> { System.out.println("Работа завершена."); return; }
                    default -> System.out.println("Неизвестная команда.");
                }
            } catch (RuntimeException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("\n1 - добавить  2 - показать все  3 - изменить по id");
        System.out.println("4 - удалить по id  5 - поиск по бренду  0 - выход");
        System.out.print("Выберите действие: ");
    }

    private void add() {
        MusicalInstrument instrument = readFields(null);
        System.out.println("Добавлено: " + service.add(instrument));
    }

    private void update() {
        long id = readLong("Введите id изменяемой записи: ");
        MusicalInstrument instrument = readFields(id);
        System.out.println(service.update(instrument) ? "Запись изменена." : "Запись не найдена.");
    }

    private void delete() {
        long id = readLong("Введите id удаляемой записи: ");
        System.out.println(service.delete(id) ? "Запись удалена." : "Запись не найдена.");
    }

    private void search() {
        System.out.print("Введите часть названия бренда: ");
        print(service.searchByBrand(scanner.nextLine().trim()));
    }

    private MusicalInstrument readFields(Long id) {
        String name = readText("Название: ");
        String brand = readText("Бренд: ");
        String type = readText("Тип инструмента (гитара, пианино...): ");
        String material = readText("Материал: ");
        BigDecimal price = readDecimal("Цена: ");
        int quantity = readInt("Количество: ");
        return new MusicalInstrument(id, name, brand, type, material, price, quantity);
    }

    private String readText(String prompt) {
        System.out.print(prompt);
        String value = scanner.nextLine().trim();
        if (value.isBlank()) throw new IllegalArgumentException("текстовое поле не может быть пустым");
        return value;
    }

    private long readLong(String prompt) {
        System.out.print(prompt);
        return Long.parseLong(scanner.nextLine().trim());
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        int value = Integer.parseInt(scanner.nextLine().trim());
        if (value < 0) throw new IllegalArgumentException("количество не может быть отрицательным");
        return value;
    }

    private BigDecimal readDecimal(String prompt) {
        System.out.print(prompt);
        BigDecimal value = new BigDecimal(scanner.nextLine().trim().replace(',', '.'));
        if (value.signum() < 0) throw new IllegalArgumentException("цена не может быть отрицательной");
        return value;
    }

    private void print(List<MusicalInstrument> instruments) {
        if (instruments.isEmpty()) {
            System.out.println("Записей не найдено.");
        } else {
            instruments.forEach(System.out::println);
        }
    }
}
