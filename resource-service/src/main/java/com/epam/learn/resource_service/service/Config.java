package com.epam.learn.resource_service.service;

import com.epam.learn.resource_service.service.meta_data.MetaDataMapper;
import com.epam.learn.resource_service.service.meta_data.TikaMetaDataExtractor;
import org.apache.tika.metadata.Metadata;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.function.Function;

@Configuration
public class Config {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Function<byte[], Metadata> mp3MetadataExtractor() {
        return new TikaMetaDataExtractor();
    }

    @Bean
    public Function<Metadata, Map<String, String>> songMetadataMapper() {
        return new MetaDataMapper();
    }

    @Bean
    public Function<byte[], Map<String, String>> songInfoExtractor() {
        return mp3MetadataExtractor().andThen(songMetadataMapper());
    }
}