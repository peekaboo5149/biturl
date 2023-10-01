package org.nerds.biturl.expection;

public class InternalServerError extends RuntimeException {
    public InternalServerError(Exception e) {
        super(e);
    }
}
