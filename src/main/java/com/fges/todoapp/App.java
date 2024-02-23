package com.fges.todoapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.cli.*;

/**
 * Représente une tâche dans la liste des TODOs.
 */
class TodoItem {
    public String task; // Le libellé de la tâche
    public boolean done; // Indique si la tâche est accomplie

    // Constructeur par défaut nécessaire pour la désérialisation JSON
    public TodoItem() {
    }

    // Constructeur pour créer une nouvelle tâche avec un libellé et un état (accomplie ou non)
    public TodoItem(String task, boolean done) {
        this.task = task;
        this.done = done;
    }
}

/**
 * Interface définissant les opérations de base pour la gestion des fichiers de tâches TODO.
 */
interface TodoFileManager {
    void insert(String todo, boolean isDone, Path filePath) throws IOException; // Insère une nouvelle tâche
    void list(Path filePath, boolean onlyDone) throws IOException; // Liste les tâches, avec filtre optionnel sur les tâches accomplies
    List<TodoItem> readAll(Path filePath) throws IOException; // Lit toutes les tâches depuis le fichier
}

/**
 * Gestionnaire pour les fichiers au format JSON.
 */
class JsonFileManager implements TodoFileManager {
    private final ObjectMapper mapper = new ObjectMapper(); // ObjectMapper pour la sérialisation/désérialisation JSON

    @Override
    public void insert(String todo, boolean isDone, Path filePath) throws IOException {
        List<TodoItem> todos = readAll(filePath);
        todos.add(new TodoItem(todo, isDone));
        Files.writeString(filePath, mapper.writeValueAsString(todos));
    }

    @Override
    public void list(Path filePath, boolean onlyDone) throws IOException {
        List<TodoItem> todos = readAll(filePath);
        todos.stream()
            .filter(item -> !onlyDone || item.done)
            .forEach(item -> System.out.println("- " + (item.done ? "Done: " : "") + item.task));
    }

    @Override
    public List<TodoItem> readAll(Path filePath) throws IOException {
        if (Files.exists(filePath)) {
            return mapper.readValue(Files.readString(filePath), new TypeReference<List<TodoItem>>() {});
        }
        return new ArrayList<>();
    }
}

/**
 * Gestionnaire pour les fichiers au format CSV.
 */
class CsvFileManager implements TodoFileManager {
    @Override
    public void insert(String todo, boolean isDone, Path filePath) throws IOException {
        List<TodoItem> todos = readAll(filePath);
        todos.add(new TodoItem(todo, isDone));
        String content = todos.stream()
                .map(item -> "\"" + item.task + "\"," + item.done)
                .collect(Collectors.joining("\n"));
        Files.writeString(filePath, content);
    }

    @Override
    public void list(Path filePath, boolean onlyDone) throws IOException {
        List<TodoItem> todos = readAll(filePath);
        todos.stream()
            .filter(item -> !onlyDone || item.done)
            .forEach(item -> System.out.println("- " + (item.done ? "Done: " : "") + item.task));
    }

    @Override
    public List<TodoItem> readAll(Path filePath) throws IOException {
        if (Files.exists(filePath)) {
            String content = Files.readString(filePath);
            return Arrays.stream(content.split("\n"))
                    .map(line -> line.split(",", -1))
                    .filter(parts -> parts.length >= 2)
                    .map(parts -> new TodoItem(parts[0].replace("\"", ""), Boolean.parseBoolean(parts[1])))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}

/**
 * Application principale pour la gestion des tâches TODO.
 */
public class App {
    public static void main(String[] args) {
        System.exit(exec(args));
    }

    /**
     * Gère l'exécution des commandes passées en argument.
     * @param args Les arguments de la ligne de commande.
     * @return Le code de sortie de l'application.
     */
    public static int exec(String[] args) {
        Options cliOptions = new Options();
        CommandLineParser parser = new DefaultParser();
        cliOptions.addRequiredOption("s", "source", true, "File containing the todos");
        cliOptions.addOption("d", "done", false, "Mark todo as done or list only done todos");
        cliOptions.addOption("o", "output", true, "Output file for migration");

        CommandLine cmd = null;
        try {
            cmd = parser.parse(cliOptions, args);
        } catch (ParseException ex) {
            System.err.println("Error parsing command line options: " + ex.getMessage());
            return 1;
        }

        String fileName = cmd.getOptionValue("s");
        boolean isDone = cmd.hasOption("d");
        String outputFileName = cmd.getOptionValue("o");
        List<String> positionalArgs = cmd.getArgList();

        if (positionalArgs.isEmpty()) {
            System.err.println("Missing Command");
            return 1;
        }

        String command = positionalArgs.get(0);
        Path filePath = Path.of(fileName);
        Path outputPath = outputFileName != null ? Path.of(outputFileName) : null;

        try {
            TodoFileManager manager = getFileManager(filePath);

            switch (command) {
                case "insert":
                    if (positionalArgs.size() < 2) {
                        System.err.println("Missing TODO name");
                        return 1;
                    }
                    String todo = positionalArgs.get(1);
                    manager.insert(todo, isDone, filePath);
                    break;
                case "list":
                    manager.list(filePath, isDone);
                    break;
                case "migrate":
                    if (outputPath == null) {
                        System.err.println("Missing output file for migration");
                        return 1;
                    }
                    migrate(manager, getFileManager(outputPath), filePath, outputPath);
                    break;
                default:
                    System.err.println("Unknown command: " + command);
                    return 1;
            }
        } catch (IOException ex) {
            System.err.println("An I/O error occurred: " + ex.getMessage());
            return 1;
        }

        System.out.println("Done.");
        return 0;
    }

    /**
     * Migre les tâches TODO d'un fichier vers un autre, en respectant le format spécifié.
     * @param sourceManager Gestionnaire du fichier source.
     * @param outputManager Gestionnaire du fichier de destination.
     * @param sourcePath Chemin du fichier source.
     * @param outputPath Chemin du fichier de destination.
     * @throws IOException Si une erreur d'entrée/sortie se produit.
     */
    private static void migrate(TodoFileManager sourceManager, TodoFileManager outputManager, Path sourcePath, Path outputPath) throws IOException {
        List<TodoItem> todos = sourceManager.readAll(sourcePath);
        for (TodoItem todo : todos) {
            outputManager.insert(todo.task, todo.done, outputPath);
        }
    }

    /**
     * Sélectionne le gestionnaire de fichiers approprié en fonction de l'extension du fichier.
     * @param filePath Le chemin du fichier pour lequel un gestionnaire est requis.
     * @return Une instance de TodoFileManager adaptée au type de fichier.
     */
    private static TodoFileManager getFileManager(Path filePath) {
        if (filePath.toString().endsWith(".json")) {
            return new JsonFileManager();
        } else if (filePath.toString().endsWith(".csv")) {
            return new CsvFileManager();
        } else {
            throw new IllegalArgumentException("Unsupported file format: " + filePath);
        }
    }
}
