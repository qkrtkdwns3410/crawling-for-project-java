package com.crawling.crawlingforprojectjava.jobplanet.service;

import com.crawling.crawlingforprojectjava.jobplanet.repository.JobPlanetCompanyInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.crawling.crawlingforprojectjava.jobplanet.service
 * fileName       : JobPlanetCompanyService
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
@Slf4j
public class JobPlanetCompanyService {
    private final JobPlanetCompanyInfoRepository jobPlanetCompanyInfoRepository;
    
    public void saveCompanyInfos(List<planet>) {
        log.info("saveCompanyInfo");
    }
}
