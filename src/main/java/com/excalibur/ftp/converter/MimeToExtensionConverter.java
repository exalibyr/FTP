package com.excalibur.ftp.converter;

import com.excalibur.ftp.exception.Exceptions;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class MimeToExtensionConverter implements Converter<String, String> {

    @Override
    public String convert(String source) throws IllegalArgumentException {
        Assert.notNull(source, "MIME type can't be null!");
        switch (source) {
            case MediaType.IMAGE_GIF_VALUE: return "gif";
            case MediaType.IMAGE_JPEG_VALUE: return "jpg";
            case MediaType.IMAGE_PNG_VALUE: return "png";
            default: throw Exceptions.illegalArgument(source);
        }
    }
}
