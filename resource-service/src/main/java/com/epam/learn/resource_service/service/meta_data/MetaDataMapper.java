package com.epam.learn.resource_service.service.meta_data;

import org.apache.tika.metadata.Metadata;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MetaDataMapper implements Function<Metadata, Map<String, String>> {
    private static final String NAME = "title";
    private static final String ARTIST = "xmpDM:artist";
    private static final String ALBUM = "xmpDM:album";
    private static final String LENGTH = "xmpDM:duration";
    private static final String YEAR = "xmpDM:releaseDate";

    @Override
    public Map<String, String> apply(Metadata metadata) {
        Map<String, String> map = new HashMap<>();
        map.put("name", getOrDefault(metadata, NAME));
        map.put("artist", getOrDefault(metadata, ARTIST));
        map.put("album", getOrDefault(metadata, ALBUM));
        map.put("length", getOrDefault(metadata, LENGTH));
        map.put("year", getOrDefault(metadata, YEAR));
        return map;
    }

    private String getOrDefault(Metadata metadata, String key) {
        String value = metadata.get(key);
        return value != null ? value : "";
    }
}