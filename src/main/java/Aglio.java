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

        String[] tasks = new String[100];
        boolean[] isDone = new boolean[100]; // tracks whether each task is marked as done
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
                        String statusIcon = isDone[i] ? "[X]" : "[ ]";
                        System.out.println(" " + (i + 1) + "." + statusIcon + " " + tasks[i]);
                    }
                } else if (line.startsWith("mark ")) {
                    // Mark a task as done by its 1-based number
                    int index = Integer.parseInt(line.substring(5)) - 1;
                    isDone[index] = true;
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   [X] " + tasks[index]);
                } else {
                    // Store the task and confirm
                    tasks[taskCount] = line;
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
