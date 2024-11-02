package com.epam.learn.resource_service.service.meta_data;

import java.util.Map;
import java.util.function.Function;

@FunctionalInterface
public interface MetaDataExtractor extends Function<byte[], Map<String, String>> {
}