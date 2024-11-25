package com.crawling.crawlingforprojectjava.jobplanet.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

/**
 * packageName    : com.crawling.crawlingforprojectjava.jobplanet.domain
 * fileName       : JobPlanetCompanyInfo
 * author         : sjunpark
 * date           : 24. 11. 25.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 24. 11. 25.        sjunpark       최초 생성
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@BatchSize(size = 500)
//회사명과 평점 개수가 같은 데이터는 중복되지 않도록 설정
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"companyName", "ratingCount"}))
@Getter
@Builder
public class JobPlanetCompanyInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String companyName;
    
    @Column(nullable = false, length = 6)
    private double rating;
    
    @Column(nullable = false, length = 10)
    private int ratingCount;
    
    @Column(nullable = false, length = 4000)
    private String link;
    
    public static JobPlanetCompanyInfo of(String companyName, double rating, int ratingCount, String link) {
        return JobPlanetCompanyInfo.builder()
                .companyName(companyName)
                .rating(rating)
                .ratingCount(ratingCount)
                .link(link)
                .build();
    }
    
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        JobPlanetCompanyInfo that = (JobPlanetCompanyInfo) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }
    
    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
