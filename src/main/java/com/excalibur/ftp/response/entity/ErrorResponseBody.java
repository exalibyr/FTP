package com.excalibur.ftp.response.entity;

public class ErrorResponseBody extends ResponseBody {

    public ErrorResponseBody() {
    }

    public ErrorResponseBody(boolean success, String message) {
        super(success, message);
    }

}
