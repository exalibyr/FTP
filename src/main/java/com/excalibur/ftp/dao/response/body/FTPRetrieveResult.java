package com.excalibur.ftp.dao.response.body;

import java.util.List;
import java.util.Map;

public class FTPRetrieveResult extends Result{

    private Map<String, byte[]> fileKeyFileContentMap;

    public FTPRetrieveResult(boolean success, List<String> errors, Map<String, byte[]> fileKeyFileContentMap) {
        super(success, errors);
        this.fileKeyFileContentMap = fileKeyFileContentMap;
    }

    public Map<String, byte[]> getFileKeyFileContentMap() {
        return fileKeyFileContentMap;
    }

    public void setFileKeyFileContentMap(Map<String, byte[]> fileKeyFileContentMap) {
        this.fileKeyFileContentMap = fileKeyFileContentMap;
    }

}
