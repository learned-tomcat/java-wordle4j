package ru.yandex.practicum;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Wordle {

    private static final int MAX_STEPS = 6;
    private static final String LOG_FILE_NAME = "game.log";
    private static final String DICTIONARY_FILE_NAME = "words_ru.txt";

    public static void main(String[] args) {
        try (
                PrintWriter log = new PrintWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(LOG_FILE_NAME, true),
                                StandardCharsets.UTF_8
                        )
                );
                Scanner scanner = new Scanner(System.in)
        ) {
            runGame(scanner, log);
        } catch (Exception e) {
            System.out.println("Произошла ошибка программы.");
        }
    }

    private static void runGame(Scanner scanner, PrintWriter log) {
        try {
            log.println("Запуск игры Wordle");

            WordleDictionaryLoader loader = new WordleDictionaryLoader(log);
            WordleDictionary dictionary = loader.load(DICTIONARY_FILE_NAME);
            WordleGame game = new WordleGame(dictionary, MAX_STEPS, log);

            printGreeting();

            while (!game.isGameOver()) {
                System.out.println("Осталось попыток: " + game.getRemainingSteps());
                System.out.print("Введите слово: ");

                String input = scanner.nextLine();

                if (input.isBlank()) {
                    System.out.println("Подсказка: " + game.getHint());
                    continue;
                }

                try {
                    System.out.println(game.makeMove(input));
                } catch (InvalidWordException | WordNotFoundInDictionaryException e) {
                    System.out.println(e.getMessage());
                }
            }

            printGameResult(game);

            log.println("Игра завершена");
            log.println("Победа: " + game.isWin());
        } catch (Exception e) {
            log.println("Критическая ошибка программы:");
            e.printStackTrace(log);
            System.out.println("Произошла ошибка. Подробности записаны в лог-файл.");
        }
    }

    private static void printGreeting() {
        System.out.println("Игра Wordle");
        System.out.println("Угадайте слово из 5 букв");
        System.out.println("Пустой ввод = подсказка");
        System.out.println();
    }

    private static void printGameResult(WordleGame game) {
        if (game.isWin()) {
            System.out.println("Вы победили!");
        } else {
            System.out.println("Вы проиграли!");
        }

        System.out.println("Загаданное слово: " + game.getAnswer());
    }
}
