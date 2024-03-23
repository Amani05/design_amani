package com.fges.todoapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonFileManager implements TodoFileManager {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void insert(Todoitem todoitem, Path filePath) throws IOException {
        List<Todoitem> todos = readAll(filePath);
        todos.add(todoitem);
        Files.writeString(filePath, mapper.writeValueAsString(todos));
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
            return mapper.readValue(Files.readString(filePath), new TypeReference<List<Todoitem>>() {});
        }
        return new ArrayList<>();
    }
}