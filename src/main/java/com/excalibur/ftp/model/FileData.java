package com.excalibur.ftp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class FileData {

    @ToString.Exclude
    private byte[] content;
    private String name;
    private String extension;
    private String contentType;
    private String location;
    private String path;

}
