package com.Aeb.AebDMS.app.advicer.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

public class BaseException extends RuntimeException {
    @Setter @Getter
    private HttpStatusCode status;

    public BaseException(HttpStatusCode status) {
        this.status = status;
    }

    public BaseException(String message,HttpStatusCode status) {
        super(message);
        this.status = status;
    }

    public BaseException(String message, Throwable cause,HttpStatusCode status) {
        super(message, cause);
        this.status = status;
    }

    public BaseException(Throwable cause,HttpStatusCode status) {
        super(cause);
        this.status = status;
    }

}
