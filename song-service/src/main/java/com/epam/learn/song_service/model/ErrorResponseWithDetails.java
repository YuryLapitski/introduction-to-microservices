package com.epam.learn.song_service.model;

import java.util.Map;

public record ErrorResponseWithDetails(String message, Map<String, String> details, String code) {
}