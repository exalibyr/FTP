package com.excalibur.ftp.exception;

import com.excalibur.ftp.error.HttpError;

public final class Exceptions {

    private Exceptions(){}

    public static FTPIntegrationClientException unauthorized(String messageTemplate, Object... args) {
        return new FTPIntegrationClientException(
                new HttpError(
                        401,
                        "Unauthorized",
                        messageTemplate != null? String.format(messageTemplate, args) : null
                )
        );
    }

    public static FTPIntegrationClientException unauthorized() {
        return unauthorized(null);
    }

    public static FTPIntegrationClientException internalServerError(String messageTemplate, Object... args) {
        return new FTPIntegrationClientException(
                new HttpError(
                        500,
                        "Internal server error",
                        messageTemplate != null? String.format(messageTemplate, args) : null
                )
        );
    }

    public static FTPIntegrationClientException internalServerError() {
        return internalServerError(null);
    }

    public static RuntimeException runtimeException(String messageTemplate, Object... args) {
        return new RuntimeException(messageTemplate == null ? "" : String.format(messageTemplate, args));
    }

    public static IllegalArgumentException illegalArgument(Object actualValue) {
        return new IllegalArgumentException(String.format("Argument can't be %s", actualValue));
    }

    public static IllegalArgumentException nullArgument() {
        return new IllegalArgumentException("Argument can't be null");
    }

    public static FTPIntegrationClientException badRequest(String messageTemplate, Object... args) {
        return new FTPIntegrationClientException(
                new HttpError(
                        400,
                        "Bad request",
                        messageTemplate != null? String.format(messageTemplate, args) : null
                )
        );
    }

}
