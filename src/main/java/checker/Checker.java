package checker;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.lines;

public class Checker {
    private final long minWordLength = 3;
    Path songFile = Paths.get("src/main/resources/data.txt");
    Path excludeFile = Paths.get("src/main/resources/excludeList.txt");
    private long wordCount = 0;

    public void checkTotalWords() {
        try {
            try (Stream<String> fileLines = lines(songFile, Charset.defaultCharset())) {
                wordCount = fileLines.flatMap(line -> Arrays.stream(line.split(" "))).count();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        System.out.println("Number of words in data.txt: " + wordCount);
    }

    public void printShortWords() {
        try {
            Stream<String> fileLines = lines(songFile, Charset.defaultCharset());
            wordCount = fileLines.flatMap(line -> Arrays.stream
                    (line.split("\\s+"))).filter(x -> x.length() <= minWordLength).count();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        System.out.println("Number of words less than three letter: " + wordCount);
    }

    public void showSong() {
        try {
            Stream<String> fileLines = lines(songFile, Charset.defaultCharset());
            fileLines.forEach(line -> System.out.println(filterSensor(line)));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @org.jetbrains.annotations.NotNull
    private String filterSensor(String text) {
        StringBuilder textBuilder = new StringBuilder();
        StringBuilder regex = new StringBuilder("(?i)");
        try {
            lines(excludeFile)
                    .forEach(badWord -> regex.append(badWord).append("|"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textBuilder
                .append(text.replaceAll(regex.toString().replaceFirst("[|]$", ""), "***"))
                .toString();
    }

    public void printPopularWords() {
        System.out.println("The most popular words: ");
        try {
            Stream<String> fileLines = lines(songFile, Charset.defaultCharset());
            long topWords = 5;
            fileLines.flatMap(line -> Arrays.stream
                    (line.split(" ")))
                    .collect(Collectors.groupingBy(Function.identity(),
                            Collectors.counting()))
                    .keySet().stream()
                    .limit(topWords)
                    .collect(Collectors.toList())
                    .forEach(System.out::println);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}