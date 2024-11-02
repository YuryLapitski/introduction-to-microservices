package com.epam.learn.resource_service.service.meta_data;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.Function;

public class TikaMetaDataExtractor implements Function<byte[], Metadata> {
    @Override
    public Metadata apply(byte[] audioData) {
        var metadata = new Metadata();
        try (var inputStream = new ByteArrayInputStream(audioData)) {
            var parser = new Mp3Parser();
            var handler = new BodyContentHandler();
            var context = new ParseContext();
            parser.parse(inputStream, handler, metadata, context);
        } catch (IOException | SAXException | TikaException e) {
            throw new RuntimeException("Failed to parse audio data", e);
        }

        return metadata;
    }
}