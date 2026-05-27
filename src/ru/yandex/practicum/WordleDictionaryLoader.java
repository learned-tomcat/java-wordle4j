package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class WordleDictionaryLoader {

    private final PrintWriter log;

    public WordleDictionaryLoader(PrintWriter log) {
        if (log == null) {
            throw new IllegalArgumentException("Лог не должен быть null");
        }

        this.log = log;
    }

    public WordleDictionary load(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new DictionaryLoadingException("Не указано имя файла словаря");
        }

        Set<String> uniqueWords = new LinkedHashSet<>();

        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(fileName),
                                StandardCharsets.UTF_8
                        )
                )
        ) {
            String line;

            while ((line = reader.readLine()) != null) {
                String normalized = WordleDictionary.normalize(line);

                if (WordleDictionary.isValidRussianWord(normalized)) {
                    uniqueWords.add(normalized);
                }
            }
        } catch (IOException e) {
            log.println("Ошибка загрузки словаря: " + e.getMessage());
            throw new DictionaryLoadingException("Не удалось загрузить словарь", e);
        }

        if (uniqueWords.isEmpty()) {
            log.println("Словарь пуст или не содержит слов из 5 русских букв");
            throw new EmptyDictionaryException("Словарь пуст");
        }

        List<String> words = new ArrayList<>(uniqueWords);

        log.println("Словарь загружен. Слов: " + words.size());

        return new WordleDictionary(words);
    }
}