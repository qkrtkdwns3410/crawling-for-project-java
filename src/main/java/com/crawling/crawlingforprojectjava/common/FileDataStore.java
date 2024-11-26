package com.crawling.crawlingforprojectjava.common;

import com.crawling.crawlingforprojectjava.jobplanet.domain.JobPlanetCompanyInfo;
import lombok.Getter;

/**
 * packageName    : com.crawling.crawlingforprojectjava.common
 * fileName       : FileDataStore
 * author         : ipeac
 * date           : 24. 11. 26.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 24. 11. 26.        ipeac       최초 생성
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
