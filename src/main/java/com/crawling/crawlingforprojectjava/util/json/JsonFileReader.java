package com.crawling.crawlingforprojectjava.util.json;

import com.crawling.crawlingforprojectjava.jobplanet.domain.JobPlanetCompanyInfo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
public class JsonFileReader {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static void readFrom(@NonNull String jsonFilePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory factory = objectMapper.getFactory();
        JsonParser parser = factory.createParser(Files.newInputStream(Path.of(jsonFilePath)));
        
        // JSON 배열 시작 확인
        if (parser.nextToken() == JsonToken.START_ARRAY) {
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                // 각 객체를 읽어서 MyEntity로 변환
                JobPlanetCompanyInfo companyInfo = objectMapper.readValue(parser, JobPlanetCompanyInfo.class);
            }
        }
        parser.close();
    }
    
}
