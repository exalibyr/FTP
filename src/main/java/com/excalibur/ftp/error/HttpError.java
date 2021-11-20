package com.excalibur.ftp.error;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class HttpError extends Error {

    protected int statusCode;
    protected String statusReason;

    public HttpError(int statusCode, String statusReason, String message) {
        super(message);
        this.statusCode = statusCode;
        this.statusReason = statusReason;
    }

    public HttpError() {
        super(null);
        this.statusCode = 500;
        this.statusReason = "Internal server error";
    }
}
