package aglio;

import java.util.Scanner;

/**
 * Aglio is a personal chatbot that stores tasks entered by the user,
 * lists them on request, and exits when the user types "bye".
 */
public class Aglio {
    private static final int MAX_TASKS = 100;
    private static final String DIVIDER = "____________________________________________________________";

    public static void main(String[] args) {
        String banner = "    _         _ _       \n"
                + "   / \\   __ _| (_) ___  \n"
                + "  / _ \\ / _` | | |/ _ \\ \n"
                + " / ___ \\ (_| | | | (_) |\n"
                + "/_/   \\_\\__, |_|_|\\___/ \n"
                + "        |___/           \n";

        // Greet the user
        System.out.println(DIVIDER);
        System.out.println(banner);
        System.out.println("Hello, I am Aglio.");
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER);

        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        // try-with-resources ensures the scanner is closed when done
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.equals("bye")) {
                    break;
                }

                System.out.println(DIVIDER);

                try {
                    if (line.equals("list")) {
                        // Print all stored tasks with their done status
                        System.out.println(" Here are the tasks in your list:");
                        for (int i = 0; i < taskCount; i++) {
                            System.out.println(" " + (i + 1) + "." + tasks[i]);
                        }
                    } else if (line.startsWith("mark ")) {
                        int index = parseTaskIndex(line.substring(5), taskCount);
                        tasks[index].markAsDone();
                        System.out.println(" Nice! I've marked this task as done:");
                        System.out.println("   " + tasks[index]);
                    } else if (line.startsWith("unmark ")) {
                        int index = parseTaskIndex(line.substring(7), taskCount);
                        tasks[index].markAsNotDone();
                        System.out.println(" OK, I've marked this task as not done yet:");
                        System.out.println("   " + tasks[index]);
                    } else if (line.equals("todo")
                            || (line.startsWith("todo ") && line.substring(5).trim().isEmpty())) {
                        throw new AglioException("The description of a todo cannot be empty.");
                    } else if (taskCount >= MAX_TASKS && (line.startsWith("todo ")
                            || line.startsWith("deadline ") || line.startsWith("event "))) {
                        throw new AglioException("Task list is full. You cannot add more than "
                                + MAX_TASKS + " tasks.");
                    } else if (line.startsWith("todo ")) {
                        String description = line.substring(5);
                        tasks[taskCount] = new Todo(description);
                        taskCount++;
                        printTaskAdded(tasks[taskCount - 1], taskCount);
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
                        tasks[taskCount] = new Deadline(parts[0], parts[1]);
                        taskCount++;
                        printTaskAdded(tasks[taskCount - 1], taskCount);
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
                        tasks[taskCount] = new Event(parts[0], timeParts[0], timeParts[1]);
                        taskCount++;
                        printTaskAdded(tasks[taskCount - 1], taskCount);
                    } else {
                        throw new AglioException(
                                "I'm sorry, but I don't know what that means :-(");
                    }
                } catch (AglioException e) {
                    System.out.println(" OOPS!!! " + e.getMessage());
                }

                System.out.println(DIVIDER);
            }
        }

        // Say goodbye
        System.out.println(DIVIDER);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(DIVIDER);
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

    /** Prints a confirmation message after a task has been added. */
    private static void printTaskAdded(Task task, int taskCount) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }
}
