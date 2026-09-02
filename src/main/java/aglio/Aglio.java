package aglio;

import java.util.Scanner;

/**
 * Aglio is a personal chatbot that stores tasks entered by the user,
 * lists them on request, and exits when the user types "bye".
 */
public class Aglio {
    public static void main(String[] args) {
        String banner = "    _         _ _       \n"
                + "   / \\   __ _| (_) ___  \n"
                + "  / _ \\ / _` | | |/ _ \\ \n"
                + " / ___ \\ (_| | | | (_) |\n"
                + "/_/   \\_\\__, |_|_|\\___/ \n"
                + "        |___/           \n";

        // Greet the user
        System.out.println("____________________________________________________________");
        System.out.println(banner);
        System.out.println("Hello, I am Aglio.");
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________");

        Task[] tasks = new Task[100];
        int taskCount = 0;

        // try-with-resources ensures the scanner is closed when done
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.equals("bye")) {
                    break;
                }

                System.out.println("____________________________________________________________");

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
                } else if (line.startsWith("todo ")) {
                    String description = line.substring(5);
                    tasks[taskCount] = new Todo(description);
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + tasks[taskCount]);
                    taskCount++;
                    System.out.println(" Now you have " + taskCount + " tasks in the list.");
                } else if (line.startsWith("deadline ")) {
                    String rest = line.substring(9);
                    String[] parts = rest.split(" /by ", 2);
                    tasks[taskCount] = new Deadline(parts[0], parts[1]);
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + tasks[taskCount]);
                    taskCount++;
                    System.out.println(" Now you have " + taskCount + " tasks in the list.");
                } else {
                    // Store the task and confirm
                    tasks[taskCount] = new Task(line);
                    taskCount++;
                    System.out.println(" added: " + line);
                }

                System.out.println("____________________________________________________________");
            }
        }

        // Say goodbye
        System.out.println("____________________________________________________________");
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");
    }
}
