package com.excalibur.ftp.util;

import com.excalibur.ftp.entity.response.DeleteResponseBody;
import com.excalibur.ftp.entity.response.StoreResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {

    public static ResponseEntity<StoreResponseBody> buildStoreResponse(StoreResponseBody responseBody) {
        return new ResponseEntity<>(
                responseBody,
                responseBody.isSuccess() ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    public static ResponseEntity<DeleteResponseBody> buildDeleteResponse(DeleteResponseBody responseBody) {
        return new ResponseEntity<>(
                responseBody,
                responseBody.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
