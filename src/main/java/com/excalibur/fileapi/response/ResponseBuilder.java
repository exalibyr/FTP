package com.excalibur.fileapi.response;

import com.excalibur.fileapi.response.entity.DeleteResponseBody;
import com.excalibur.fileapi.response.entity.ErrorResponseBody;
import com.excalibur.fileapi.response.entity.StoreResponseBody;
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

    public static ResponseEntity<ErrorResponseBody> buildErrorResponse(String message) {
        return new ResponseEntity<>(
            new ErrorResponseBody(false, message),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
