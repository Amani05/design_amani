package com.fges.todoapp;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.cli.*;

public class App {

    public static void main(String[] args) {
        System.exit(exec(args));
    }

    public static int exec(String[] args) {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        options.addRequiredOption("s", "source", true, "File containing the todos");
        options.addOption("d", "done", false, "Mark todo as done or list only done todos");
        options.addOption("o", "output", true, "Output file for migration");

        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println("Error parsing command line options: " + e.getMessage());
            return 1;
        }

        String fileName = cmd.getOptionValue("s");
        boolean isDone = cmd.hasOption("d");
        String outputFileName = cmd.getOptionValue("o");
        List<String> extraArgs = cmd.getArgList();

        if (extraArgs.isEmpty()) {
            System.err.println("Missing command");
            return 1;
        }

        String command = extraArgs.get(0);
        Path filePath = Path.of(fileName);
        TodoFileManager fileManager = getFileManager(filePath);

        try {
            switch (command) {
                case "insert":
                    if (extraArgs.size() < 2) {
                        System.err.println("Missing TODO description");
                        return 1;
                    }
                    String todoDescription = extraArgs.get(1);
                    fileManager.insert(new Todoitem(todoDescription, isDone), filePath);
                    break;
                case "list":
                    fileManager.list(filePath, isDone);
                    break;
                case "migrate":
                    if (outputFileName == null) {
                        System.err.println("Missing output file for migration");
                        return 1;
                    }
                    Path outputPath = Path.of(outputFileName);
                    TodoFileManager outputFileManager = getFileManager(outputPath);
                    migrate(fileManager, outputFileManager, filePath, outputPath);
                    break;
                default:
                    System.err.println("Unknown command: " + command);
                    return 1;
            }
        } catch (IOException e) {
            System.err.println("An I/O error occurred: " + e.getMessage());
            return 1;
        }

        System.out.println("Operation completed successfully.");
        return 0;
    }

    private static TodoFileManager getFileManager(Path filePath) {
        if (filePath.toString().endsWith(".json")) {
            return new JsonFileManager();
        } else if (filePath.toString().endsWith(".csv")) {
            return new CsvFileManager();
        } else {
            throw new IllegalArgumentException("Unsupported file format: " + filePath);
        }
    }

    private static void migrate(TodoFileManager sourceManager, TodoFileManager outputManager, Path sourcePath, Path outputPath) throws IOException {
        // Assuming this is the logic for migration
        List<Todoitem> todos = sourceManager.readAll(sourcePath);
        for (Todoitem todo : todos) {
            outputManager.insert(todo, outputPath);
        }
    }
}
