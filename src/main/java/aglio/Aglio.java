package aglio;

import aglio.exception.AglioException;
import aglio.storage.Storage;
import aglio.task.Deadline;
import aglio.task.Event;
import aglio.task.Task;
import aglio.task.TaskList;
import aglio.task.Todo;
import aglio.ui.Ui;

import java.io.IOException;

/**
 * Aglio is a personal chatbot that stores tasks entered by the user,
 * lists them on request, and exits when the user types "bye".
 */
public class Aglio {

    public static void main(String[] args) {
        Ui ui = new Ui();

        ui.showGreeting();

        Storage storage = new Storage("data/aglio.txt");
        TaskList tasks;
        try {
            tasks = new TaskList(storage.load());
        } catch (AglioException e) {
            ui.showLoadingError(e.getMessage());
            tasks = new TaskList();
        }

        while (ui.hasNextCommand()) {
            String line = ui.readCommand();

            if (line.equals("bye")) {
                break;
            }

            ui.showDivider();

            try {
                if (line.equals("list")) {
                    ui.showTaskList(tasks);
                } else if (line.startsWith("mark ")) {
                    int index = parseTaskIndex(line.substring(5), tasks.getSize());
                    tasks.getTask(index).markAsDone();
                    ui.showMarkDone(tasks.getTask(index));
                    saveTasks(storage, tasks, ui);
                } else if (line.startsWith("unmark ")) {
                    int index = parseTaskIndex(line.substring(7), tasks.getSize());
                    tasks.getTask(index).markAsNotDone();
                    ui.showMarkNotDone(tasks.getTask(index));
                    saveTasks(storage, tasks, ui);
                } else if (line.startsWith("delete ")) {
                    int index = parseTaskIndex(line.substring(7), tasks.getSize());
                    Task removedTask = tasks.deleteTask(index);
                    ui.showDelete(removedTask, tasks.getSize());
                    saveTasks(storage, tasks, ui);
                } else if (line.equals("todo")
                        || (line.startsWith("todo ") && line.substring(5).trim().isEmpty())) {
                    throw new AglioException("The description of a todo cannot be empty.");
                } else if (line.startsWith("todo ")) {
                    String description = line.substring(5);
                    tasks.addTask(new Todo(description));
                    ui.showTaskAdded(tasks.getTask(tasks.getSize() - 1), tasks.getSize());
                    saveTasks(storage, tasks, ui);
                } else if (line.equals("deadline")
                        || (line.startsWith("deadline ") && line.substring(9).trim().isEmpty())) {
                    throw new AglioException("The description of a deadline cannot be empty.");
                } else if (line.startsWith("deadline ")) {
                    String rest = line.substring(9);
                    String[] parts = rest.split(" /by ", 2);
                    if (parts.length < 2) {
                        throw new AglioException("A deadline requires a /by clause.\n"
                                + " Usage: deadline <description> /by <date>");
                    }
                    tasks.addTask(new Deadline(parts[0], parts[1]));
                    ui.showTaskAdded(tasks.getTask(tasks.getSize() - 1), tasks.getSize());
                    saveTasks(storage, tasks, ui);
                } else if (line.equals("event")
                        || (line.startsWith("event ") && line.substring(6).trim().isEmpty())) {
                    throw new AglioException("The description of an event cannot be empty.");
                } else if (line.startsWith("event ")) {
                    String rest = line.substring(6);
                    String[] parts = rest.split(" /from ", 2);
                    if (parts.length < 2) {
                        throw new AglioException("An event requires /from and /to clauses.\n"
                                + " Usage: event <description> /from <start> /to <end>");
                    }
                    String[] timeParts = parts[1].split(" /to ", 2);
                    if (timeParts.length < 2) {
                        throw new AglioException("An event requires a /to clause.\n"
                                + " Usage: event <description> /from <start> /to <end>");
                    }
                    tasks.addTask(new Event(parts[0], timeParts[0], timeParts[1]));
                    ui.showTaskAdded(tasks.getTask(tasks.getSize() - 1), tasks.getSize());
                    saveTasks(storage, tasks, ui);
                } else {
                    throw new AglioException(
                            "I'm sorry, but I don't know what that means :-(");
                }
            } catch (AglioException e) {
                ui.showError(e.getMessage());
            }

            ui.showDivider();
        }

        ui.showGoodbye();
        ui.close();
    }

    /**
     * Parses a task index string and validates that it is within range.
     *
     * @param input     the raw string after "mark " or "unmark "
     * @param taskCount the current number of tasks
     * @return the zero-based index
     * @throws AglioException if the input is not a number or is out of range
     */
    private static int parseTaskIndex(String input, int taskCount) throws AglioException {
        int index;
        try {
            index = Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
            throw new AglioException(
                    "Please provide a valid task number. Usage: mark <task number>");
        }
        if (index < 0 || index >= taskCount) {
            throw new AglioException("Task number " + (index + 1)
                    + " does not exist. You have " + taskCount + " tasks.");
        }
        return index;
    }

    /**
     * Saves all tasks to disk, printing a warning via the Ui if the write fails.
     *
     * @param storage the storage handler
     * @param tasks   the task list to save
     * @param ui      the ui handler for displaying errors
     */
    private static void saveTasks(Storage storage, TaskList tasks, Ui ui) {
        try {
            storage.save(tasks.getTasks());
        } catch (IOException e) {
            ui.showSaveError(e.getMessage());
        }
    }
}
