package com.excalibur.ftp.exception;

import com.excalibur.ftp.error.HttpError;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FTPIntegrationClientException extends RuntimeException {

    protected HttpError error;

}
