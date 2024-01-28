import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

interface TodoRepository {
    String readTodos(Path filePath);

    void writeTodos(Path filePath, String content);
}

class JsonTodoRepository implements TodoRepository {
    @Override
    public String readTodos(Path filePath) {
        try {
            String fileContent = Files.exists(filePath) ? Files.readString(filePath) : "";
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(fileContent);
            if (actualObj instanceof MissingNode) {
                // Node was not recognized
                actualObj = JsonNodeFactory.instance.arrayNode();
            }
            return actualObj.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file", e);
        }
    }

    @Override
    public void writeTodos(Path filePath, String content) {
        try {
            Files.writeString(filePath, content);
        } catch (IOException e) {
            throw new RuntimeException("Error writing JSON file", e);
        }
    }
}

class CsvTodoRepository implements TodoRepository {
    @Override
    public String readTodos(Path filePath) {
        try {
            return Files.exists(filePath) ? Files.readString(filePath) : "";
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
    }

    @Override
    public void writeTodos(Path filePath, String content) {
        try {
            if (!content.endsWith("\n") && !content.isEmpty()) {
                content += "\n";
            }
            Files.writeString(filePath, content);
        } catch (IOException e) {
            throw new RuntimeException("Error writing CSV file", e);
        }
    }
}

interface TodoManager {
    void execute(String command, List<String> positionalArgs);
}

class TodoManagerImpl implements TodoManager {
    private final TodoRepository todoRepository;

    public TodoManagerImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public void execute(String command, List<String> positionalArgs) {
        if (command.equals("insert")) {
            insert(positionalArgs);
        } else if (command.equals("list")) {
            list();
        } else {
            System.err.println("Unknown command");
        }
    }

    private void insert(List<String> positionalArgs) {
        if (positionalArgs.size() < 2) {
            System.err.println("Missing TODO name");
        } else {
            String todo = positionalArgs.get(1);
            String todos = todoRepository.readTodos(getFilePath());
            todos += todo;
            todoRepository.writeTodos(getFilePath(), todos);
        }
    }

    private void list() {
        String todos = todoRepository.readTodos(getFilePath());
        if (getFilePath().endsWith(".json")) {
            // JSON
            listJsonTodos(todos);
        } else if (getFilePath().endsWith(".csv")) {
            // CSV
            listCsvTodos(todos);
        }
    }

    private void listJsonTodos(String todos) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(todos);
        if (actualObj instanceof MissingNode) {
            // Node was not recognized
            actualObj = JsonNodeFactory.instance.arrayNode();
        }

        if (actualObj instanceof ArrayNode arrayNode) {
            arrayNode.forEach(node -> System.out.println("- " + node.toString()));
        }
    }

    private void listCsvTodos(String todos) {
        System.out.println(Arrays.stream(todos.split("\n"))
                .map(todo -> "- " + todo)
                .collect(Collectors.joining("\n"))
        );
    }

    private Path getFilePath() {
        CommandLine cmd = parseCommandLine();
        String fileName = cmd.getOptionValue("s");
        return Paths.get(fileName);
    }

    private CommandLine parseCommandLine() {
        Options cliOptions = new Options();
        cliOptions.addRequiredOption("s", "source", true, "File containing the todos");
        CommandLineParser parser = new DefaultParser();

        try {
            return parser.parse(cliOptions, getArgs());
        } catch (ParseException ex) {
            System.err.println("Fail to parse arguments: " + ex.getMessage());
            System.exit(1);
            return null; // Unreachable, added to satisfy the compiler
        }
    }

    private String[] getArgs() {
        return new String[0]; // Replace with actual method to retrieve command line arguments
    }
}

/**
 * Hello world!
 */
public class App {
    /**
     * Do not change this method
     */
    public static void main(String[] args) {
        TodoRepository todoRepository = determineTodoRepository(args);
        TodoManager todoManager = new TodoManagerImpl(todoRepository);

        System.exit(todoManager.execute(args));
    }

    private static TodoRepository determineTodoRepository(String[] args) {
        CommandLine cmd = parseCommandLine(args);
        String fileName = cmd.getOptionValue("s");

        if (fileName.endsWith(".json")) {
            return new JsonTodoRepository();
        } else if (fileName.endsWith(".csv")) {
            return new CsvTodoRepository();
        } else {
            throw new IllegalArgumentException("Unsupported file format");
        }
    }

    private static CommandLine parseCommandLine(String[] args) {
        Options cliOptions = new Options();
        cliOptions.addRequiredOption("s", "source", true, "File containing the todos");
        CommandLineParser parser = new DefaultParser();

        try {
            return parser.parse(cliOptions, args);
        } catch (ParseException ex) {
            System.err.println("Fail to parse arguments: " + ex.getMessage());
            System.exit(1);
            return null; // Unreachable, added to satisfy the compiler
        }
    }
}

