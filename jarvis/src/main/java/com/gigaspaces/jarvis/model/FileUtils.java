package com.gigaspaces.jarvis.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {

    public static void processFiles(Path path, Predicate<Path> pathFilter, Function<String, String> lineMapper) throws IOException {
        try (Stream<Path> tree = Files.walk(path)) {
            tree.filter(pathFilter).forEach(p -> processLines(p, lineMapper));
        }
    }

    public static void processLines(Path path, Function<String, String> lineMapper) {
        try {
            Files.write(path, Files.lines(path).map(lineMapper).collect(Collectors.toList()));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to add canonical url to " + path, e);
        }
    }

    public static String lineAppender(String line, Predicate<String> predicate, String extraLine) {
        return predicate.test(line) ? line + System.lineSeparator() + extraLine : line;
    }

}
