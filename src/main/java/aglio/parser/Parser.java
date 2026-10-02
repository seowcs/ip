package aglio.parser;

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
 * Deals with making sense of user commands and executing them.
 * Each recognised command is parsed, validated, and carried out
 * by interacting with the given TaskList, Ui, and Storage.
 */
public class Parser {

    /**
     * Parses and executes a single user command.
     *
     * @param command the raw command string entered by the user
     * @param tasks   the task list to operate on
     * @param ui      the ui for displaying responses
     * @param storage the storage for persisting changes
     * @throws AglioException if the command is invalid or malformed
     */
    public static void parse(String command, TaskList tasks, Ui ui,
                             Storage storage) throws AglioException {
        if (command.equals("list")) {
            ui.showTaskList(tasks);
        } else if (command.startsWith("mark ")) {
            int index = parseTaskIndex(command.substring(5), tasks.getSize());
            tasks.getTask(index).markAsDone();
            ui.showMarkDone(tasks.getTask(index));
            saveTasks(storage, tasks, ui);
        } else if (command.startsWith("unmark ")) {
            int index = parseTaskIndex(command.substring(7), tasks.getSize());
            tasks.getTask(index).markAsNotDone();
            ui.showMarkNotDone(tasks.getTask(index));
            saveTasks(storage, tasks, ui);
        } else if (command.startsWith("delete ")) {
            int index = parseTaskIndex(command.substring(7), tasks.getSize());
            Task removedTask = tasks.deleteTask(index);
            ui.showDelete(removedTask, tasks.getSize());
            saveTasks(storage, tasks, ui);
        } else if (command.equals("todo")
                || (command.startsWith("todo ") && command.substring(5).trim().isEmpty())) {
            throw new AglioException("The description of a todo cannot be empty.");
        } else if (command.startsWith("todo ")) {
            String description = command.substring(5);
            tasks.addTask(new Todo(description));
            ui.showTaskAdded(tasks.getTask(tasks.getSize() - 1), tasks.getSize());
            saveTasks(storage, tasks, ui);
        } else if (command.equals("deadline")
                || (command.startsWith("deadline ") && command.substring(9).trim().isEmpty())) {
            throw new AglioException("The description of a deadline cannot be empty.");
        } else if (command.startsWith("deadline ")) {
            String rest = command.substring(9);
            String[] parts = rest.split(" /by ", 2);
            if (parts.length < 2) {
                throw new AglioException("A deadline requires a /by clause.\n"
                        + " Usage: deadline <description> /by <date>");
            }
            tasks.addTask(new Deadline(parts[0], parts[1]));
            ui.showTaskAdded(tasks.getTask(tasks.getSize() - 1), tasks.getSize());
            saveTasks(storage, tasks, ui);
        } else if (command.equals("event")
                || (command.startsWith("event ") && command.substring(6).trim().isEmpty())) {
            throw new AglioException("The description of an event cannot be empty.");
        } else if (command.startsWith("event ")) {
            String rest = command.substring(6);
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
    }

    /**
     * Parses a task index string and validates that it is within range.
     *
     * @param input     the raw string after the command keyword (e.g. "mark ")
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

    /** Saves all tasks to disk, printing a warning via the Ui if the write fails. */
    private static void saveTasks(Storage storage, TaskList tasks, Ui ui) {
        try {
            storage.save(tasks.getTasks());
        } catch (IOException e) {
            ui.showSaveError(e.getMessage());
        }
    }
}
