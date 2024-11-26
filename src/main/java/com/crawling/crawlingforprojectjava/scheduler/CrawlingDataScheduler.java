package com.crawling.crawlingforprojectjava.scheduler;

import com.crawling.crawlingforprojectjava.jobplanet.facade.JobPlanetDataFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.crawling.crawlingforprojectjava.scheduler
 * fileName       : CrawlingDataScheduler
 * author         : ipeac
 * date           : 24. 11. 26.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 24. 11. 26.        ipeac       최초 생성
 */
@Component
@RequiredArgsConstructor
public class CrawlingDataScheduler {
    private final JobPlanetDataFacade jobPlanetDataFacade;
    
    /**
     * 매일 0시에 크롤링 데이터를 처리한다. 이전 작업이 수행된 후 2초 후에 다음 작업을 수행한다.
     */
    @Scheduled(cron = "0 0 0 * * ?", fixedDelay = 2000)
    public void crawlingData() {
        jobPlanetDataFacade.processCrawlingData();
    }
}
