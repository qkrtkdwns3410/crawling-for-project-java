package com.crawling.crawlingforprojectjava.crawling_data;

import com.crawling.crawlingforprojectjava.jobplanet.repository.JobPlanetCompanyInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.crawling.crawlingforprojectjava.crawling_data
 * fileName       : JobPlanetCrawlingDataStrategy
 * author         : sjunpark
 * date           : 24. 11. 26.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 24. 11. 26.        sjunpark       최초 생성
 */
@Service
@RequiredArgsConstructor
public class JobPlanetCrawlingDataStrategy implements CrawlingStrategy {
    private final JobPlanetCompanyInfoRepository jobPlanetCompanyInfoRepository;
    
    @Override
    public void saveCrawlingData() {
        
    }
}
