package com.crawling.crawlingforprojectjava.util.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : com.crawling.crawlingforprojectjava.util
 * fileName       : JsonFileReader
 * author         : sjunpark
 * date           : 24. 11. 25.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 24. 11. 25.        sjunpark       최초 생성
 */
@Slf4j
public class JsonFileReader {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static <T> List<T> readFrom(@NonNull String jsonFilePath, @NonNull Class<T> clazz) {
        List<T> entities = new ArrayList<>();
        
        JsonFactory factory = objectMapper.getFactory();
        
        try (JsonParser parser = factory.createParser(Files.newInputStream(Path.of(jsonFilePath)));) {
            // JSON 배열 시작 확인
            if (parser.nextToken() == JsonToken.START_ARRAY) {
                while (parser.nextToken() != JsonToken.END_ARRAY) {
                    T info = objectMapper.readValue(parser, clazz);
                    entities.add(info);
                }
            }
            
        } catch (IOException e) {
            log.error("JsonFileReader.readFrom() : {}", e.getMessage());
        }
        
        return entities;
    }
}
