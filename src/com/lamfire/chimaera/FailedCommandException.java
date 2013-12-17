package com.lamfire.chimaera;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 13-11-12
 * Time: 上午10:19
 * To change this template use File | Settings | File Templates.
 */
public class FailedCommandException extends Exception {

    public FailedCommandException() {
    }

    public FailedCommandException(String message) {
        super(message);
    }

    public FailedCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedCommandException(Throwable cause) {
        super(cause);
    }
}
