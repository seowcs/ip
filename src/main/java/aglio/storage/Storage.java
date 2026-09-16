package aglio.storage;

import aglio.exception.AglioException;
import aglio.task.Deadline;
import aglio.task.Event;
import aglio.task.Task;
import aglio.task.Todo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Handles saving and loading tasks to and from a file on disk.
 * The file uses a pipe-delimited format, one task per line.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates a Storage that reads from and writes to the given file path.
     * The path is resolved in an OS-independent way via {@link Paths#get}.
     *
     * @param filePath relative path to the data file, e.g. "data/aglio.txt"
     */
    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Saves all tasks to the data file, creating parent directories if needed.
     * Overwrites the file contents each time.
     *
     * @param tasks     the array of tasks
     * @param taskCount the number of tasks currently in the array
     * @throws IOException if the file cannot be written
     */
    public void save(Task[] tasks, int taskCount) throws IOException {
        Files.createDirectories(filePath.getParent());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < taskCount; i++) {
            sb.append(tasks[i].toFileString());
            sb.append(System.lineSeparator());
        }
        Files.writeString(filePath, sb.toString());
    }

    /**
     * Loads tasks from the data file into the given array.
     * Returns 0 if the file does not exist (first run).
     *
     * @param tasks the array to populate
     * @return the number of tasks loaded
     * @throws AglioException if the file exists but contains malformed data
     */
    public int load(Task[] tasks) throws AglioException {
        if (!Files.exists(filePath)) {
            return 0;
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new AglioException("Error reading save file: " + e.getMessage());
        }

        int count = 0;
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            tasks[count] = parseLine(line);
            count++;
        }
        return count;
    }

    /**
     * Parses a single pipe-delimited line into a Task object.
     *
     * @param line a line from the data file, e.g. "T | 0 | borrow book"
     * @return the corresponding Task
     * @throws AglioException if the line is malformed
     */
    private Task parseLine(String line) throws AglioException {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new AglioException("Corrupted line in save file: " + line);
        }

        String type = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        String description = parts[2].trim();

        Task task;
        switch (type) {
        case "T":
            task = new Todo(description);
            break;
        case "D":
            if (parts.length < 4) {
                throw new AglioException("Corrupted deadline in save file: " + line);
            }
            task = new Deadline(description, parts[3].trim());
            break;
        case "E":
            if (parts.length < 5) {
                throw new AglioException("Corrupted event in save file: " + line);
            }
            task = new Event(description, parts[3].trim(), parts[4].trim());
            break;
        default:
            throw new AglioException("Unknown task type in save file: " + type);
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
