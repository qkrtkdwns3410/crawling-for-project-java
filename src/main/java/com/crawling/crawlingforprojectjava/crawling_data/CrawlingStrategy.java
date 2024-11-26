package com.crawling.crawlingforprojectjava.crawling_data;

import com.crawling.crawlingforprojectjava.jobplanet.domain.JobPlanetCompanyInfo;

import java.util.List;

/**
 * packageName    : com.crawling.crawlingforprojectjava.crawling_data
 * fileName       : strategy
 * author         : sjunpark
 * date           : 24. 11. 26.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 24. 11. 26.        sjunpark       최초 생성
 */
public interface CrawlingStrategy {
    void saveCrawlingData(List<JobPlanetCompanyInfo> datas);
}
