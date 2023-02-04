package com.excalibur.fileapi.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MediaType {
    JPEG("image/jpeg", ".jpg"),
    PNG("image/png", ".png");

    private final String name;
    private final String extension;

    public static String getExtension(String name) {
        for (MediaType mediaType : MediaType.values()) {
            if (mediaType.getName().equals(name)) {
                return mediaType.getExtension();
            }
        }
        return null;
    }
}
