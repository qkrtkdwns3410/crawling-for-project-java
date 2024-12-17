package com.crawling.crawlingforprojectjava.jobplanet.service;

import com.crawling.crawlingforprojectjava.jobplanet.domain.JobPlanetCompanyInfo;
import com.crawling.crawlingforprojectjava.jobplanet.repository.JobPlanetCompanyInfoRepository;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BeanArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FailoverIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.customizer.Values;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.arbitraries.IntegerArbitrary;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

/**
 * packageName    : com.crawling.crawlingforprojectjava.jobplanet.service
 * fileName       : JobPlanetCrawlingDataStrategyTest
 * author         : ipeac
 * date           : 24. 11. 26.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 24. 11. 26.        ipeac       최초 생성
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("잡플래닛 크롤링 데이터 저장 전략 테스트")
class JobPlanetCrawlingDataStrategyTest {
    static final FixtureMonkey FIXTURE_MONKEY = FixtureMonkey.builder()
            .objectIntrospector(new FailoverIntrospector(
                    Arrays.asList(
                            FieldReflectionArbitraryIntrospector.INSTANCE,
                            BeanArbitraryIntrospector.INSTANCE,
                            BuilderArbitraryIntrospector.INSTANCE
                    )
            ))
            .defaultNotNull(true)
            .build();
    
    private JobPlanetCrawlingDataStrategy jobPlanetCrawlingDataStrategy;
    
    @Autowired
    private JobPlanetCompanyInfoRepository jobPlanetCompanyInfoRepository;
    
    @BeforeEach
    void setUp() {
        jobPlanetCrawlingDataStrategy = new JobPlanetCrawlingDataStrategy(jobPlanetCompanyInfoRepository);
    }
    
    @Test
    @DisplayName("크롤링 데이터 저장 테스트 - 실제 전체 데이터 안 밀어넣어도 되지만 테스트 코드 양이 많지 않을 듯..")
    void When_SaveCrawlingData_Expect_SaveAll() {
        // given
        // id 필드를 null로 설정하고 객체 리스트 생성
        // 유니크한 회사명과 평점 개수를 만들기 위해 index를 사용
        IntegerArbitrary integers = Arbitraries.integers();
        
        final List<JobPlanetCompanyInfo> jobPlanetCompanyInfos = FIXTURE_MONKEY.giveMeBuilder(JobPlanetCompanyInfo.class)
                .setNull("id")
                .set("companyName", "companyName" + Values.unique(integers::sample))
                .set("ratingCount", Values.unique(integers::sample))
                .sampleList(1500);
        
        // when
        jobPlanetCrawlingDataStrategy.saveCrawlingData(jobPlanetCompanyInfos);
        
        // then
        final List<JobPlanetCompanyInfo> actual = jobPlanetCompanyInfoRepository.findAll();
        Assertions.assertThat(actual).hasSize(1500);
    }
    
    @Test
    @DisplayName("Jsoup 데이터 크롤링 테스트")
    void When_A1_Expect_Success() {
        // given
        // when
        jobPlanetCrawlingDataStrategy.a1();
        
        // then
        jobPlanetCompanyInfoRepository.findAll().forEach(System.out::println);
    }
}
