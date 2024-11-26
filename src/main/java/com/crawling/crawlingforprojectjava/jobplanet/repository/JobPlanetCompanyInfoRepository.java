package com.crawling.crawlingforprojectjava.jobplanet.repository;

import com.crawling.crawlingforprojectjava.jobplanet.domain.JobPlanetCompanyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPlanetCompanyInfoRepository extends JpaRepository<JobPlanetCompanyInfo, Long> {
}
