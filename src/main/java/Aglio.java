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
                    // Mark a task as done by its 1-based number
                    int index = Integer.parseInt(line.substring(5)) - 1;
                    tasks[index].markAsDone();
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + tasks[index]);
                } else if (line.startsWith("unmark ")) {
                    // Mark a task as not done by its 1-based number
                    int index = Integer.parseInt(line.substring(7)) - 1;
                    tasks[index].markAsNotDone();
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + tasks[index]);
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
