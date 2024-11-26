package com.crawling.crawlingforprojectjava.common;

import com.crawling.crawlingforprojectjava.jobplanet.domain.JobPlanetCompanyInfo;
import lombok.Getter;

/**
 * 크롤링 데이터 위치와, 변환 타입을 관리하는 Enum
 */
@Getter
public enum FileDataStore {
    JOB_PLANET_CRAWLING_FILE_PATH("crawling/jobplanet/company_data.json", JobPlanetCompanyInfo.class);
    
    private final String filePath;
    private final Class<?> clazz;
    
    <T> FileDataStore(String filePath, Class<T> clazz) {
        this.filePath = filePath;
        this.clazz = clazz;
    }
    
    @SuppressWarnings("unchecked")
    public <T> Class<T> getClazz() {
        return (Class<T>) clazz;
    }
}
