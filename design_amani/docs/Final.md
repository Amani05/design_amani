# L3 design pattern report

- **Firstname**: [Amani]
- **Lastname**: [YAHIA BEY]


# Answers to Project Documentation Questions

## How can a newcomer add a new command

To add a new command to the project, follow these steps:

1. Open the `App.java` file located in the `src/com/fges/todoapp` directory.
2. Inside the `exec` method, add a new case in the switch statement to handle the new command.
3. Implement the logic for the new command, including parsing any necessary arguments and executing the desired functionality.
4. Optionally, update the documentation or help message for the command in the `final.md` file to inform other contributors about its usage.

## How can a newcomer add a new file-based datasource

To add a new file-based data source to the project, follow these steps:

1. Create a new class that implements the `TodoFileManager` interface.
2. Implement the required methods (`insert`, `list`, `readAll`) to interact with the file-based data source.
3. Ensure that the new class handles file I/O operations correctly and adheres to the specified file format (JSON or CSV).
4. Update the `App.java` file to use the new data source when necessary, depending on the command executed.

## How can a newcomer add a non-file-based datasource

To add a new non-file-based data source to the project, follow these steps:

1. Create a new class that implements the `TodoDataManager` interface.
2. Implement the required methods (`insert`, `list`, `readAll`) to interact with the non-file-based data source.
3. Ensure that the new class handles data storage and retrieval operations correctly according to the chosen data source (e.g., database, in-memory storage).
4. Update the `App.java` file to use the new data manager when necessary, depending on the command executed.

## How can a newcomer add a new attribute to a Todo

To add a new attribute to the `Todo` class, follow these steps:

1. Open the `Todoitem.java` file located in the `src/com/fges/todoapp` directory.
2. Add a new private field to represent the new attribute along with its getter and setter methods.
3. Update the constructor to initialize the new attribute if necessary.
4. Modify any existing methods that interact with `Todo` objects to handle the new attribute appropriately.

## How can a newcomer add a new interface to the project

To add a new interface to the project, follow these steps:

1. Create a new Java interface with the desired methods and functionality.
2. Define the contract and purpose of the interface in its documentation comments.
3. Implement the interface in one or more classes within the project as needed.
4. Update any existing classes or methods to use the new interface where appropriate.
5. Ensure that the interface and its implementations follow standard naming conventions and are well-documented for clarity.
