package com.crawling.crawlingforprojectjava.jobplanet.service;

import com.crawling.crawlingforprojectjava.crawling_data_strategy.CrawlingStrategy;
import com.crawling.crawlingforprojectjava.jobplanet.domain.JobPlanetCompanyInfo;
import com.crawling.crawlingforprojectjava.jobplanet.repository.JobPlanetCompanyInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
@Slf4j
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
    
    //데이터 크롤링 jsoup 로 수행하기
    public void a1() {
        // 웹 페이지 가져오기
        try {
            Document doc = Jsoup.connect("https://example.com").get();
        } catch (IOException e) {
            log.error("{}", e.getMessage());
        }
    }
}
