package aglio;

import aglio.exception.AglioException;
import aglio.parser.Parser;
import aglio.storage.Storage;
import aglio.task.TaskList;
import aglio.ui.Ui;

/**
 * Aglio is a personal chatbot that stores tasks entered by the user,
 * lists them on request, and exits when the user types "bye".
 */
public class Aglio {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Creates an Aglio chatbot that persists tasks to the given file path.
     *
     * @param filePath relative path to the data file, e.g. "data/aglio.txt"
     */
    public Aglio(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (AglioException e) {
            ui.showLoadingError(e.getMessage());
            tasks = new TaskList();
        }
    }

    /** Runs the main command loop until the user types "bye". */
    public void run() {
        ui.showGreeting();

        while (ui.hasNextCommand()) {
            String line = ui.readCommand();

            if (line.equals("bye")) {
                break;
            }

            ui.showDivider();

            try {
                Parser.parse(line, tasks, ui, storage);
            } catch (AglioException e) {
                ui.showError(e.getMessage());
            }

            ui.showDivider();
        }

        ui.showGoodbye();
        ui.close();
    }

    public static void main(String[] args) {
        new Aglio("data/aglio.txt").run();
    }
}
