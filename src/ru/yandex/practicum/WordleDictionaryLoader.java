package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {

    private final PrintWriter log;

    public WordleDictionaryLoader(PrintWriter log) {

        this.log = log;
    }

    public WordleDictionary load(String fileName)
            throws DictionaryLoadingException,
            EmptyDictionaryException {

        List<String> words = new ArrayList<>();

        try (
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        new FileInputStream(fileName),
                                        StandardCharsets.UTF_8
                                )
                        )
        ) {

            String line;

            while ((line = reader.readLine()) != null) {

                String normalized =
                        WordleDictionary.normalize(line);

                if (normalized.length() == 5
                        && WordleDictionary
                        .isValidRussianWord(normalized)) {

                    words.add(normalized);
                }
            }

        } catch (IOException e) {

            log.println("Ошибка загрузки словаря");

            throw new DictionaryLoadingException(
                    "Не удалось загрузить словарь"
            );
        }

        if (words.isEmpty()) {

            throw new EmptyDictionaryException(
                    "Словарь пуст"
            );
        }

        log.println("Словарь загружен. Слов: "
                + words.size());

        return new WordleDictionary(words);
    }
}
