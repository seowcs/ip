package aglio;

/**
 * Represents an error specific to the Aglio chatbot,
 * such as invalid user input or exceeding the task limit.
 */
public class AglioException extends Exception {
    public AglioException(String message) {
        super(message);
    }
}
