package im.jiadongy.di.core.exception;

/**
 * Created by jiadongy on 16/7/8.
 */
public class SimpleDIException extends Exception {

    public SimpleDIException(String message, Throwable cause) {
        super(message, cause);
    }

    public SimpleDIException(String message) {
        super(message);
    }
}
