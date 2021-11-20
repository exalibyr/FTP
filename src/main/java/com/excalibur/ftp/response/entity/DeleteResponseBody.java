package com.excalibur.ftp.response.entity;

@Deprecated
public class DeleteResponseBody extends ResponseBody {

    public DeleteResponseBody(boolean success, String message) {
        super(success, message);
    }
}
