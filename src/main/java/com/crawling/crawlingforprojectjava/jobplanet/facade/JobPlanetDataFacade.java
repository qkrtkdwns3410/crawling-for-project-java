package com.crawling.crawlingforprojectjava.jobplanet.facade;

import com.crawling.crawlingforprojectjava.common.FileDataStore;
import com.crawling.crawlingforprojectjava.crawling_data_strategy.CrawlingStrategy;
import com.crawling.crawlingforprojectjava.jobplanet.domain.JobPlanetCompanyInfo;
import com.crawling.crawlingforprojectjava.util.json.JsonFileReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * packageName    : com.crawling.crawlingforprojectjava.jobplanet.facade
 * fileName       : JobPlanetDataFacade
 * author         : ipeac
 * date           : 24. 11. 26.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 24. 11. 26.        ipeac       최초 생성
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JobPlanetDataFacade {
    private final CrawlingStrategy jobPlanetCrawlingDataStrategy;
    
    public void processCrawlingData() {
        final List<JobPlanetCompanyInfo> jobPlanetCompanyInfos = JsonFileReader.readFrom(FileDataStore.JOB_PLANET_CRAWLING_FILE_PATH.getFilePath(), FileDataStore.JOB_PLANET_CRAWLING_FILE_PATH.getClazz());
        jobPlanetCrawlingDataStrategy.saveCrawlingData(jobPlanetCompanyInfos);
    }
}
