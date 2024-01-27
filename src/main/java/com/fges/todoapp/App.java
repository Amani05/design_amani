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

public class TodoApp {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        try {
            System.exit(run(args));
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            System.exit(1);
        }
    }

    private static int run(String[] args) throws IOException {
        CommandLine cmd = parseCommandLine(args);

        String fileName = cmd.getOptionValue("s");
        Path filePath = Paths.get(fileName);
        String fileContent = getFileContent(filePath);

        List<String> positionalArgs = cmd.getArgList();
        if (positionalArgs.isEmpty()) {
            System.err.println("Missing Command");
            return 1;
        }

        String command = positionalArgs.get(0);

        switch (command) {
            case "insert":
                return handleInsertCommand(positionalArgs, fileName, fileContent, filePath);

            case "list":
                handleListCommand(fileName, fileContent);
                break;

            default:
                System.err.println("Unknown command: " + command);
                return 1;
        }

        System.err.println("Done.");
        return 0;
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
            return null; // Unreachable, but required for compilation
        }
    }

    private static String getFileContent(Path filePath) throws IOException {
        if (Files.exists(filePath)) {
            return Files.readString(filePath);
        }
        return "";
    }

    private static int handleInsertCommand(List<String> positionalArgs, String fileName, String fileContent, Path filePath) throws IOException {
        if (positionalArgs.size() < 2) {
            System.err.println("Missing TODO name");
            return 1;
        }

        String todo = positionalArgs.get(1);

        if (fileName.endsWith(".json")) {
            handleJsonInsert(fileContent, todo, filePath);
        } else if (fileName.endsWith(".csv")) {
            handleCsvInsert(fileContent, todo, filePath);
        }

        return 0;
    }

    private static void handleJsonInsert(String fileContent, String todo, Path filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(fileContent);
        if (actualObj instanceof MissingNode) {
            actualObj = JsonNodeFactory.instance.arrayNode();
        }

        if (actualObj instanceof ArrayNode arrayNode) {
            arrayNode.add(todo);
        }

        Files.writeString(filePath, actualObj.toString());
    }

    private static void handleCsvInsert(String fileContent, String todo, Path filePath) throws IOException {
        if (!fileContent.endsWith("\n") && !fileContent.isEmpty()) {
            fileContent += "\n";
        }
        fileContent += todo;

        Files.writeString(filePath, fileContent);
    }

    private static void handleListCommand(String fileName, String fileContent) {
        if (fileName.endsWith(".json")) {
            handleJsonList(fileContent);
        } else if (fileName.endsWith(".csv")) {
            handleCsvList(fileContent);
        }
    }

    private static void handleJsonList(String fileContent) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(fileContent);
        if (actualObj instanceof MissingNode) {
            actualObj = JsonNodeFactory.instance.arrayNode();
        }

        if (actualObj instanceof ArrayNode arrayNode) {
            arrayNode.forEach(node -> System.out.println("- " + node.toString()));
        }
    }

    private static void handleCsvList(String fileContent) {
        System.out.println(Arrays.stream(fileContent.split("\n"))
                .map(todo -> "- " + todo)
                .collect(Collectors.joining("\n"))
        );
    }
}
