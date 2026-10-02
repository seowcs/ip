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
                Parser.parse(line, tasks, ui, storage);
            } catch (AglioException e) {
                ui.showError(e.getMessage());
            }

            ui.showDivider();
        }

        ui.showGoodbye();
        ui.close();
    }
}
