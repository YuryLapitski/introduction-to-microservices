package com.epam.learn.resource_service.controller;

import com.epam.learn.resource_service.service.ResourceService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @PostMapping(consumes = "audio/mpeg", produces = "application/json")
    public ResponseEntity<Map<String, Integer>> uploadResource(@RequestBody byte[] audioData) throws Exception {
        try {
            var resource = resourceService.createResource(audioData);
            Map<String, Integer> result = new HashMap<>();
            result.put("id", resource.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new Exception("An internal server error has occurred. " + e.getMessage());
        }
    }

    @GetMapping(value = "/{id}", produces = "audio/mpeg")
    public ResponseEntity<byte[]> getResource(@PathVariable int id) throws Exception {
        if (id <= 0) {
            throw new BadRequestException(String.format("Invalid value '%s' for ID. Must be a positive integer", id));
        }
        try {
            byte[] audioData = resourceService.getResourceDataById(id);
            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.parseMediaType("audio/mpeg"))
                    .body(audioData);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(String.format("The resource with ID=%s does not exist.", id));
        } catch (Exception e) {
            throw new Exception("An internal server error has occurred. " + e.getMessage());
        }
    }

    @DeleteMapping(produces = "application/json")
    public ResponseEntity<Map<String, List<Integer>>> deleteResources(@RequestParam("id") String id) throws Exception {
        if (id == null || id.isEmpty()) {
            throw new BadRequestException("ID list should not be empty");
        }
        if (id.length() >= 200) {
            throw new BadRequestException(
                    String.format("IDs string too long. Received %s characters, but must be less than 200 characters.", id.length()));
        }
        try {
            Map<String, List<Integer>> deletedIds = Map.of("ids", resourceService.deleteResources(id));
            return ResponseEntity.ok(deletedIds);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid id format. ID should be a CSV string of integer.");
        } catch (Exception e) {
            throw new Exception("An internal server error has occurred. " + e.getMessage());
        }
    }
}