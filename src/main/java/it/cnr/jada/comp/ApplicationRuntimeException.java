package it.cnr.jada.comp;

import it.cnr.jada.DetailedRuntimeException;

public class ApplicationRuntimeException extends DetailedRuntimeException {

    protected ApplicationRuntimeException() {
    }

    public ApplicationRuntimeException(String s) {
        super(s);
    }

    public ApplicationRuntimeException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ApplicationRuntimeException(Throwable throwable) {
        super(throwable);
    }
}
