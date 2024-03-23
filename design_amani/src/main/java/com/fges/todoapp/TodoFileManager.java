package com.fges.todoapp;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface TodoFileManager {
    void insert(Todoitem todoitem, Path filePath) throws IOException;
    void list(Path filePath, boolean onlyDone) throws IOException;
    List<Todoitem> readAll(Path filePath) throws IOException;
}