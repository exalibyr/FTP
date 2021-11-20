package com.excalibur.ftp.response.entity;
@Deprecated
public class ErrorResponseBody extends ResponseBody {

    public ErrorResponseBody() {
    }

    public ErrorResponseBody(boolean success, String message) {
        super(success, message);
    }

}
