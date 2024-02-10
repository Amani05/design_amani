package com.fges.todoapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TodoApp {
    public static void main(String[] args) {
        CommandLineProcessor commandLineProcessor = new CommandLineProcessor(new TodoManager());
        commandLineProcessor.process(args);
    }

    static class CommandLineProcessor {
        private final CommandExecutor commandExecutor;

        public CommandLineProcessor(CommandExecutor commandExecutor) {
            this.commandExecutor = commandExecutor;
        }

        public void process(String[] args) {
            Options cliOptions = new Options();
            CommandLineParser parser = new DefaultParser();

            cliOptions.addRequiredOption("s", "source", true, "File containing the todos");
            cliOptions.addOption(Option.builder("d")
                    .longOpt("done")
                    .desc("Flag to mark a task as done")
                    .build());

            CommandLine cmd;
            try {
                cmd = parser.parse(cliOptions, args);
            } catch (ParseException ex) {
                System.err.println("Fail to parse arguments: " + ex.getMessage());
                return;
            }

            String fileName = cmd.getOptionValue("s");

            try {
                int exitCode = commandExecutor.execute(cmd, fileName);
                System.exit(exitCode);
            } catch (IOException e) {
                System.err.println("Error occurred: " + e.getMessage());
                System.exit(1);
            }
        }
    }

    interface CommandExecutor {
        int execute(CommandLine cmd, String fileName) throws IOException;
    }

    static class TodoManager implements CommandExecutor {
        @Override
        public int execute(CommandLine cmd, String fileName) throws IOException {
            String command = cmd.getArgList().get(0);

            Path filePath = Paths.get(fileName);
            String fileContent = Files.exists(filePath) ? Files.readString(filePath) : "";

            if (cmd.hasOption("d")) {
                markTaskAsDone(cmd, fileName, filePath, fileContent);
                return 0;
            }

            if (command.equals("insert")) {
                return insertTodo(cmd, fileName, filePath, fileContent);
            }

            if (command.equals("list")) {
                listTodos(cmd, fileName, fileContent);
                return 0;
            }

            System.err.println("Unknown command: " + command);
            return 1;
        }

        private int insertTodo(CommandLine cmd, String fileName, Path filePath, String fileContent) throws IOException {
            if (cmd.getArgList().size() < 2) {
                System.err.println("Missing TODO name");
                return 1;
            }
            String todo = cmd.getArgList().get(1);

            if (fileName.endsWith(".json")) {
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
            if (fileName.endsWith(".csv")) {
                if (!fileContent.endsWith("\n") && !fileContent.isEmpty()) {
                    fileContent += "\n";
                }
                fileContent += todo;

                Files.writeString(filePath, fileContent);
            }
            return 0;
        }

        private void listTodos(CommandLine cmd, String fileName, String fileContent) {
            boolean showDoneTasks = cmd.hasOption("d");

            if (fileName.endsWith(".json")) {
                if (showDoneTasks) {
                    System.out.println("Done tasks:");
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        JsonNode actualObj = mapper.readTree(fileContent);
                        if (actualObj instanceof MissingNode) {
                            actualObj = JsonNodeFactory.instance.arrayNode();
                        }

                        if (actualObj instanceof ArrayNode arrayNode) {
                            arrayNode.forEach(node -> {

                            });
                        }
                    } catch (IOException e) {
                        System.err.println("Error reading JSON: " + e.getMessage());
                    }
                } else {
                    System.out.println("All tasks:");
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        JsonNode actualObj = mapper.readTree(fileContent);
                        if (actualObj instanceof MissingNode) {
                            actualObj = JsonNodeFactory.instance.arrayNode();
                        }

                        if (actualObj instanceof ArrayNode arrayNode) {
                            arrayNode.forEach(node -> {
                                System.out.println("- " + node.toString());
                            });
                        }
                    } catch (IOException e) {
                        System.err.println("Error reading JSON: " + e.getMessage());
                    }
                }
            }  else if (fileName.endsWith(".csv")) {
                if (showDoneTasks) {
                    System.out.println("Done tasks:");
                    Arrays.stream(fileContent.split("\n")).forEach(task -> {

                    });
                } else {
                    System.out.println("All tasks:");
                    Arrays.stream(fileContent.split("\n")).forEach(task -> {
                        System.out.println("- " + task);
                    });
                }
            }
        }

        private void markTaskAsDone(CommandLine cmd, String fileName, Path filePath, String fileContent) throws IOException {
            if (cmd.getArgList().size() < 2) {
                System.err.println("Missing task name");
                return;
            }
            String task = cmd.getArgList().get(1);

            
            String updatedFileContent = markTaskAsDoneInFileContent(task, fileContent);

            Files.writeString(filePath, updatedFileContent);

            System.out.println("Task '" + task + "' marked as done.");
        }

        private String markTaskAsDoneInFileContent(String task, String fileContent) {
            
            String[] lines = fileContent.split("\n");
            for (int i = 0; i < lines.length; i++) {
                if (lines[i].trim().equals(task)) {
                    
                    lines[i] += " [DONE]";
                    break;
                }
            }
            
            return String.join("\n", lines);
        }






        }
    }


