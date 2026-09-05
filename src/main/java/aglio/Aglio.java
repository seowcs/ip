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

                if (line.equals("list")) {
                    // Print all stored tasks with their done status
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(" " + (i + 1) + "." + tasks[i]);
                    }
                } else if (line.startsWith("mark ")) {
                    try {
                        int index = Integer.parseInt(line.substring(5)) - 1;
                        tasks[index].markAsDone();
                        System.out.println(" Nice! I've marked this task as done:");
                        System.out.println("   " + tasks[index]);
                    } catch (NumberFormatException e) {
                        System.out.println(" Please provide a valid task number. Usage: mark <task number>");
                    }
                } else if (line.startsWith("unmark ")) {
                    try {
                        int index = Integer.parseInt(line.substring(7)) - 1;
                        tasks[index].markAsNotDone();
                        System.out.println(" OK, I've marked this task as not done yet:");
                        System.out.println("   " + tasks[index]);
                    } catch (NumberFormatException e) {
                        System.out.println(" Please provide a valid task number. Usage: unmark <task number>");
                    }
                } else if (line.equals("todo")
                        || (line.startsWith("todo ") && line.substring(5).trim().isEmpty())) {
                    System.out.println(" OOPS!!! The description of a todo cannot be empty.");
                } else if (line.startsWith("todo ")) {
                    String description = line.substring(5);
                    tasks[taskCount] = new Todo(description);
                    taskCount++;
                    printTaskAdded(tasks[taskCount - 1], taskCount);
                } else if (line.equals("deadline")
                        || (line.startsWith("deadline ") && line.substring(9).trim().isEmpty())) {
                    System.out.println(" OOPS!!! The description of a deadline cannot be empty.");
                } else if (line.startsWith("deadline ")) {
                    String rest = line.substring(9);
                    String[] parts = rest.split(" /by ", 2);
                    tasks[taskCount] = new Deadline(parts[0], parts[1]);
                    taskCount++;
                    printTaskAdded(tasks[taskCount - 1], taskCount);
                } else if (line.equals("event")
                        || (line.startsWith("event ") && line.substring(6).trim().isEmpty())) {
                    System.out.println(" OOPS!!! The description of an event cannot be empty.");
                } else if (line.startsWith("event ")) {
                    String rest = line.substring(6);
                    String[] parts = rest.split(" /from ", 2);
                    String[] timeParts = parts[1].split(" /to ", 2);
                    tasks[taskCount] = new Event(parts[0], timeParts[0], timeParts[1]);
                    taskCount++;
                    printTaskAdded(tasks[taskCount - 1], taskCount);
                } else {
                    System.out.println(" OOPS!!! I'm sorry, but I don't know what that means :-(");
                }

                System.out.println(DIVIDER);
            }
        }

        // Say goodbye
        System.out.println(DIVIDER);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(DIVIDER);
    }

    /** Prints a confirmation message after a task has been added. */
    private static void printTaskAdded(Task task, int taskCount) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }
}
