package com.lamfire.chimaera;

public class ChimaeraException extends RuntimeException {


    private static final long serialVersionUID = -3021150198795799502L;

    public ChimaeraException() {
        super();
    }

    public ChimaeraException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChimaeraException(String message) {
        super(message);
    }

    public ChimaeraException(Throwable cause) {
        super(cause);
    }


}
