package com.fges.todoapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CsvFileManager implements TodoFileManager {

    @Override
    public void insert(Todoitem todoitem, Path filePath) throws IOException {
        List<Todoitem> todos = readAll(filePath);
        todos.add(todoitem);
        String content = todos.stream()
                .map(item -> "\"" + item.getTask() + "\"," + item.isDone())
                .collect(Collectors.joining("\n"));
        Files.writeString(filePath, content);
    }

    @Override
    public void list(Path filePath, boolean onlyDone) throws IOException {
        List<Todoitem> todos = readAll(filePath);
        todos.stream()
                .filter(item -> !onlyDone || item.isDone())
                .forEach(item -> System.out.println("- " + (item.isDone() ? "Done: " : "") + item.getTask()));
    }

    @Override
    public List<Todoitem> readAll(Path filePath) throws IOException {
        if (Files.exists(filePath)) {
            String content = Files.readString(filePath);
            return Arrays.stream(content.split("\n"))
                    .map(line -> line.split(",", -1))
                    .filter(parts -> parts.length >= 2)
                    .map(parts -> new Todoitem(parts[0].replace("\"", ""), Boolean.parseBoolean(parts[1])))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
