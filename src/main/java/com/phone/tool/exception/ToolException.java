package com.phone.tool.exception;

import org.springframework.http.HttpStatus;

public class ToolException extends RuntimeException{
    private int statusCode;
    private String message;

    public ToolException(int statusCode) {
        this(statusCode, reasonPhrase(statusCode));
    }

    public ToolException(int statusCode, String message) {
        super(String.format("Tool server exception Error message %s", message));
        this.statusCode= statusCode;
        this.message = message;
    }

    private static String reasonPhrase(int httpStatusCode) {
        try {
            return HttpStatus.valueOf(httpStatusCode).getReasonPhrase();
        } catch (IllegalArgumentException var2) {
            return "Status code " + httpStatusCode;
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
