package com.excalibur.ftp.service;

import java.util.Map;

public interface FileInterface<T, K> {

    default K upload(T file) {
        return null;
    }

    default Iterable<K> uploadAll(T... files) {
        return null;
    }

    default T download(K path) {
        return null;
    }

    default Map<K, T> downloadAll(K... paths) {
        return null;
    }

    default void delete(K path){}

    default Iterable<K> deleteAll(K... paths) {
        return null;
    }

}
