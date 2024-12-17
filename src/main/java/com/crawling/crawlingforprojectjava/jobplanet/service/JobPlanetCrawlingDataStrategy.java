package com.crawling.crawlingforprojectjava.jobplanet.service;

import com.crawling.crawlingforprojectjava.crawling_data_strategy.CrawlingStrategy;
import com.crawling.crawlingforprojectjava.jobplanet.domain.JobPlanetCompanyInfo;
import com.crawling.crawlingforprojectjava.jobplanet.repository.JobPlanetCompanyInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
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
    // 저장할 배치 단위
    public static final int BATCH_SIZE = 100;
    private final JobPlanetCompanyInfoRepository jobPlanetCompanyInfoRepository;
    
    @Transactional(readOnly = false)
    @Override
    public void saveCrawlingData(List<JobPlanetCompanyInfo> datas) {
        int dataSize = datas.size();
        
        for (int i = 0; i < dataSize; i += BATCH_SIZE) {
            int endIdx = Math.min(i + BATCH_SIZE, dataSize);
            jobPlanetCompanyInfoRepository.saveAll(datas.subList(i, endIdx));
            jobPlanetCompanyInfoRepository.flush();
        }
    }
    
    @Transactional(readOnly = false)
    //데이터 크롤링 jsoup 로 수행하기
    public void a1() {
        // 웹 페이지 가져오기
        int page = 1;
        
        boolean keepRunning = true;
        
        List<JobPlanetCompanyInfo> batchList = new ArrayList<>();
        
        while (keepRunning) {
            
            try {
                int currentPage = page++;
                
                Document doc = Jsoup.connect(MessageFormat.format("https://www.jobplanet.co.kr/companies?page={0}", currentPage))
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                        .header("Accept-Encoding", "gzip, deflate, br, zstd")
                        .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7,ja;q=0.6,zh-TW;q=0.5,zh;q=0.4")
                        .header("Cache-Control", "max-age=0")
                        .header("Cookie", "_jp_visitor_token=9008790b-5c71-4699-8cc3-49dd8c6bf66c; ch-veil-id=842812dc-bf11-4743-a4a5-1137c123259a; premiumInfoModalOn_190611=on; _fwb=94cxqreWJDjCK2iv3KEFpK.1701687131670; _clck=1p9rkcn%7C2%7Cfnb%7C0%7C1650; ab.storage.deviceId.79a99ab6-04d1-4666-97ba-475bf96a2b3d=g%3Ae905753c-f2ed-4c03-eb03-c7e1a587c178%7Ce%3Aundefined%7Cc%3A1701687131539%7Cl%3A1720525311830; ab.storage.userId.79a99ab6-04d1-4666-97ba-475bf96a2b3d=g%3AJPL_3350790%7Ce%3Aundefined%7Cc%3A1720525311829%7Cl%3A1720525311831;")
                        .header("Priority", "u=0, i")
                        .header("Sec-CH-UA", "\"Chromium\";v=\"130\", \"Whale\";v=\"4\", \"Not.A/Brand\";v=\"99\"")
                        .header("Sec-CH-UA-Mobile", "?0")
                        .header("Sec-CH-UA-Platform", "\"Windows\"")
                        .header("Sec-Fetch-Dest", "document")
                        .header("Sec-Fetch-Mode", "navigate")
                        .header("Sec-Fetch-Site", "same-origin")
                        .header("Sec-Fetch-User", "?1")
                        .header("Upgrade-Insecure-Requests", "1")
                        .header("User-Agent", " Chrome/130.0.0.0")
                        .method(Connection.Method.GET)
                        .get();
                
                log.info("프로세싱 중인 URL : {}", MessageFormat.format("https://www.jobplanet.co.kr/companies?page={0}", currentPage));
                
                for (int nth = 1; nth <= 10; nth++) {
                    final Elements companyNameElement = doc.select(MessageFormat.format("html > body > div:nth-of-type(1) > div:nth-of-type(2) > div > div:nth-of-type(1) > div:nth-of-type(1) > article > div > div:nth-of-type(1) > section:nth-of-type({0}) > div > div > dl:nth-of-type(1) > dt > a", nth));
                    if (companyNameElement.isEmpty()) {
                        keepRunning = false;
                        break;
                    }
                    
                    final String companyName = companyNameElement.text();
                    
                    //링크 -> companyNameElement.attr("href");
                    final String companyLink = companyNameElement.attr("href");
                    
                    //리뷰 개수
                    final String reviewCount = doc.select(MessageFormat.format("html > body > div:nth-of-type(1) > div:nth-of-type(2) > div > div:nth-of-type(1) > div:nth-of-type(1) > article > div > div:nth-of-type(1) > section:nth-of-type({0}) > div > div > dl:nth-of-type(2) > dt", nth)).text();
                    
                    //리뷰 별점
                    final String reviewStar = doc.select(MessageFormat.format("html > body > div:nth-of-type(1) > div:nth-of-type(2) > div > div:nth-of-type(1) > div:nth-of-type(1) > article > div > div:nth-of-type(1) > section:nth-of-type({0}) > div > div > dl:nth-of-type(2) > dd:nth-of-type(1) > span", nth)).text();
                    
                    
                    batchList.add(JobPlanetCompanyInfo.of(companyName, reviewStar, reviewCount, companyLink));
                    
                    if (batchList.size() >= BATCH_SIZE) {
                        saveCrawlingData(batchList);
                        batchList.clear();
                    }
                }
                
                Thread.sleep(2000);
                
            } catch (IOException e) {
                log.error("{}", e.getMessage());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
