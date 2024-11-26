package com.crawling.crawlingforprojectjava.jobplanet.service;

import com.crawling.crawlingforprojectjava.crawling_data_strategy.CrawlingStrategy;
import com.crawling.crawlingforprojectjava.jobplanet.domain.JobPlanetCompanyInfo;
import com.crawling.crawlingforprojectjava.jobplanet.repository.JobPlanetCompanyInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
@Transactional
@RequiredArgsConstructor
public class JobPlanetCrawlingDataStrategy implements CrawlingStrategy {
    private final JobPlanetCompanyInfoRepository jobPlanetCompanyInfoRepository;
    
    @Transactional(readOnly = false)
    @Override
    public void saveCrawlingData(List<JobPlanetCompanyInfo> datas) {
        final int batchSize = 500;
        final int dataSize = datas.size();
        
        for (int i = 0; i < dataSize; i += batchSize) {
            int endIdx = Math.min(i + batchSize, dataSize);
            
            jobPlanetCompanyInfoRepository.saveAll(datas.subList(i, endIdx));
            jobPlanetCompanyInfoRepository.flush();
        }
    }
}
