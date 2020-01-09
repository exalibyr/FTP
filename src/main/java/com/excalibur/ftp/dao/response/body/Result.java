package com.excalibur.ftp.dao.response.body;

import java.util.List;

public abstract class Result {

    private boolean success;
    private List<String> errors;

    Result() { }

    Result(boolean success, List<String> errors) {
        this.success = success;
        this.errors = errors;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
