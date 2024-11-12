package com.epam.learn.resource_service.service;

import com.epam.learn.resource_service.dao.ResourceRepository;
import com.epam.learn.resource_service.model.Resource;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class ResourceService {
    private static final String CREATE_SONG_ENDPOINT = "/songs";

    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private Function<byte[], Map<String, String>> mp3MetadataExtractor;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${song.service.url}")
    private String songServiceUrl;

    @Transactional
    public Resource createResource(byte[] audioData) throws IOException {
        Resource resource = new Resource();
        resource.setData(audioData);
        Resource savedResource = resourceRepository.save(resource);

        saveMetaData(audioData, savedResource);

        return savedResource;
    }

    public byte[] getResourceDataById(int id) {
        return resourceRepository.findById(id).map(Resource::getData).orElseThrow();
    }

    public List<Integer> deleteResources(String idsCSV) {
        List<Integer> ids = Arrays.stream(idsCSV.split(","))
                .map(Integer::parseInt)
                .toList();

        List<Integer> existingIds = ids.stream()
                .filter(id -> resourceRepository.existsById(id))
                .toList();

        resourceRepository.deleteAllById(existingIds);
        return existingIds;
    }

    private void saveMetaData(byte[] audioData, Resource savedResource) throws BadRequestException {
        var songMetadata = mp3MetadataExtractor.apply(audioData);
        songMetadata.put("resourceId", String.valueOf(savedResource.getId()));
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(songMetadata);
        try {
            restTemplate.postForObject(songServiceUrl + CREATE_SONG_ENDPOINT, requestEntity, Void.class);
        } catch (HttpClientErrorException ex) {
            HttpStatusCode statusCode = ex.getStatusCode();
            if (statusCode.is4xxClientError()) {
                throw new BadRequestException("Failed to create song due to validation errors: " + ex.getResponseBodyAsString());
            } else {
                throw new RuntimeException("Internal server error occurred while calling song-service");
            }
        }
    }
}