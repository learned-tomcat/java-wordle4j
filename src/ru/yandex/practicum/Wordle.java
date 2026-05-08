package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Wordle {

    private static final int MAX_STEPS = 6;

    public static void main(String[] args) {

        try (
                PrintWriter log = new PrintWriter(new FileWriter("game.log", true));
                Scanner scanner = new Scanner(System.in)
        ) {

            log.println("Запуск игры Wordle");

            WordleDictionaryLoader loader = new WordleDictionaryLoader(log);

            WordleDictionary dictionary =
                    loader.load("words_ru.txt");

            WordleGame game =
                    new WordleGame(dictionary, MAX_STEPS, log);

            System.out.println("Игра Wordle");
            System.out.println("Угадайте слово из 5 букв");
            System.out.println("Пустой ввод = подсказка");
            System.out.println();

            while (!game.isGameOver()) {

                System.out.println("Осталось попыток: "
                        + game.getRemainingSteps());

                System.out.print("Введите слово: ");

                String input = scanner.nextLine();

                if (input.isBlank()) {

                    String hint = game.getHint();

                    System.out.println("Подсказка: " + hint);
                    continue;
                }

                try {

                    String result = game.makeMove(input);

                    System.out.println(result);

                } catch (InvalidWordException
                         | WordNotFoundInDictionaryException e) {

                    System.out.println(e.getMessage());
                }
            }

            if (game.isWin()) {

                System.out.println("Вы победили!");

            } else {

                System.out.println("Вы проиграли!");
            }

            System.out.println("Загаданное слово: "
                    + game.getAnswer());

            log.println("Game over");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
